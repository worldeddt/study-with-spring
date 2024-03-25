package aop.prototypes.rtcOutBound.as.exception;


import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private CommonCode commonCode;

    public CommonException(CommonCode status) {
        super(status.getMessage());
        this.commonCode = status;
    }

    public CommonException(String resultMsg) {
        super(resultMsg);
    }

    public String getMessage() {
        return this.commonCode.getMessage();
    }
}
