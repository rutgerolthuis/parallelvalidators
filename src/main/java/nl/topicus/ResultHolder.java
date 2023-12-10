package nl.topicus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.topicus.validator.ResultCode;
import nl.topicus.validator.ResultStatus;


public record ResultHolder(ResultStatus status, ResultCode code) {
}
