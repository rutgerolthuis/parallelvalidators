package nl.topicus.validator;

import lombok.RequiredArgsConstructor;
import nl.topicus.Registratie;
import nl.topicus.ResultHolder;
import nl.topicus.SeoRuw;

@RequiredArgsConstructor
public class BlaatValidator implements Validator{
    private static final ResultCode code = ResultCode.BLAAT;
    private final ResultStatus resultStatus;
    private final Registratie registratie;

    @Override
    public ResultHolder validate() {
        try {
            Thread.sleep(Validator.DELAYTIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(registratie instanceof SeoRuw seo){
        return seo.isOk() || seo.wazzaa().equals("oeps") ? succes(code) : unsuccesful(resultStatus, code);
        }
        return unsuccesful(resultStatus, code);

    }
}
