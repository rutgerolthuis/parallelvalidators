package nl.topicus.validator;

import nl.topicus.ResultHolder;

public interface Validator {
    ResultHolder validate();
    long DELAYTIME = 50;
     default ResultHolder succes(ResultCode code){
        return new ResultHolder(ResultStatus.SUCCES, code);
    }

    default ResultHolder unsuccesful(ResultStatus status, ResultCode code){
        return new ResultHolder(status, code);
    }
 }
