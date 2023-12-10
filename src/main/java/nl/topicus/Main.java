package nl.topicus;

import nl.topicus.validator.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


public class Main {
    private final Function<Validator, ResultHolder> execValidator = Validator::validate;
    private final BiFunction<Function<Registratie,Validator>,Registratie,ResultHolder>
            initAndRunValidator = (initValidator,registratie) -> initValidator.andThen(execValidator).apply(registratie);

    public static void main(String[] args) {

        var main = new Main();
        main.run();
    }

    private void run(){
        var registratie = new SeoRuw(1L,true);

        runTests(this::runWithRegularStream, registratie);
        runTests(this::runWithParallelStream, registratie);
        runTests(this::runWithVirtualThreads, registratie);
    }

    private List<Function<Registratie,Validator>> getValidators(){

        var foo = validator(registratie -> new FooValidator(ResultStatus.WAARSCHUWING, registratie));
        var bar = validator(registratie -> new BarValidator(ResultStatus.FOUT, registratie));
        var sch = validator(registratie -> new SchaapValidator(ResultStatus.WAARSCHUWING, registratie));
        var bla = validator(registratie -> new BlaatValidator(ResultStatus.AFGEKEURD, registratie));

        List<Function<Registratie,Validator>> list = new ArrayList<>();
        for (var i=0;i<100;i++) {
            list.add(foo);
            list.add(bar);
            list.add(sch);
            list.add(bla);
        }

        return list;
    }
    private Function<Registratie,Validator> validator(Function<Registratie,Validator> functie){
        return functie;
    }

    private void runTests(BiFunction<List<Function<Registratie,Validator>> ,Registratie, List<ResultHolder>> runner,
                          Registratie registratie){
        var list = getValidators();
        long startTime = System.nanoTime();
        var results = runner.apply(list,registratie);
        System.out.println("Found number of records: " + results.size());
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("It took: " + NANOSECONDS.toMillis(totalTime) + " ms");
    }
    private List<ResultHolder> runWithParallelStream(List<Function<Registratie,Validator>> list, Registratie registratie){
        System.out.println("Running with parallel stream");
        return list.parallelStream()
                .map(f -> initAndRunValidator.apply(f,registratie))
                .toList();
    }

    private List<ResultHolder> runWithVirtualThreads(List<Function<Registratie,Validator>> list, Registratie registratie){
        List<Callable<ResultHolder>> callables =
                list.stream().map(
                        f -> (Callable<ResultHolder>) () -> (ResultHolder) initAndRunValidator.apply(f,registratie))
                        .toList();
        System.out.println("Running with Virtual Threads");
        try(ExecutorService e = Executors.newVirtualThreadPerTaskExecutor()){

            var futures = e.invokeAll(callables);
            System.out.println("Number of futures: " + futures.size());

            return futures.stream().map(resultHolderFuture -> {
                try {
                    return resultHolderFuture.get();
                } catch (InterruptedException | ExecutionException ex) {
                    throw new RuntimeException(ex);
                }
            }).toList();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private List<ResultHolder> runWithRegularStream(List<Function<Registratie,Validator>> list, Registratie registratie){
        System.out.println("Running with regular stream");
        return list.stream()
                .map(f -> initAndRunValidator.apply(f,registratie))
                .toList();

    }


}