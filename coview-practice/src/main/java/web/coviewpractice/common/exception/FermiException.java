package web.coviewpractice.common.exception;

import lombok.Getter;
import web.coviewpractice.common.enums.FermiErrorCode;

public class FermiException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    @Getter
    private FermiErrorCode fermiErrorCode;

    public FermiException(FermiErrorCode fermiErrorCode) {
        super(fermiErrorCode.getMessage());
        this.fermiErrorCode = fermiErrorCode;
    }

    public FermiException(String message, FermiErrorCode fermiErrorCode) {
        super(message);
        this.fermiErrorCode = fermiErrorCode;
    }

}