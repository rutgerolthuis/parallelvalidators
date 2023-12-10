package nl.topicus;

import nl.topicus.validator.ResultCode;
import nl.topicus.validator.ResultStatus;


public record ResultHolder(ResultStatus status, ResultCode code) {
}
