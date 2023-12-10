package nl.topicus.validator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResultCode {
    FOO("001"),
    BAR("002"),
    SCHAAP("003"),
    BLAAT("004")
    ;

    private final String code;

}
