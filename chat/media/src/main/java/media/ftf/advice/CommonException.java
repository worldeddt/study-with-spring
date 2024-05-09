package media.ftf.advice;

import lombok.Getter;
import media.ftf.enums.CommonCode;

@Getter
public class CommonException extends RuntimeException {

    private final CommonCode commonCode;

    public CommonException(CommonCode commonCode) {
        super(commonCode.getMessage());
        this.commonCode = commonCode;
    }

    public CommonException(String message, CommonCode commonCode) {
        super(message);
        this.commonCode = commonCode;
    }

}