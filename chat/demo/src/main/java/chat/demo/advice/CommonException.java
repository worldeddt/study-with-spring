package chat.demo.advice;

import lombok.Getter;


public class CommonException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    @Getter
    private final CommonCode fermiCode;

    public CommonException(CommonCode fermiCode) {
        super(fermiCode.getMessage());
        this.fermiCode = fermiCode;
    }

    public CommonException(String message, CommonCode fermiCode) {
        super(message);
        this.fermiCode = fermiCode;
    }

}