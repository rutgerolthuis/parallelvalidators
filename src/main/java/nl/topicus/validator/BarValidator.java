package nl.topicus.validator;

import lombok.RequiredArgsConstructor;
import nl.topicus.Registratie;
import nl.topicus.ResultHolder;

@RequiredArgsConstructor
public class BarValidator implements Validator{
    private static final ResultCode code = ResultCode.BAR;
    private final ResultStatus resultStatus;
    private final Registratie registratie;

    @Override
    public ResultHolder validate() {
        try {
            Thread.sleep(Validator.DELAYTIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return registratie.isOk() ? succes(code) : unsuccesful(resultStatus, code);
    }
}
