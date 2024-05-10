package media.ftf.advice.dto;

import lombok.Builder;
import lombok.Getter;
import media.ftf.enums.CommonCode;

import java.util.List;

@Getter
@Builder
public class ErrorMessage {
    private int status;
    private String code;
    private String message;
    private String destination;
    private String payload;
    private List<String> data;

    public static ErrorMessage of(CommonCode fermiCode) {
        return ErrorMessage.builder()
                .status(fermiCode.getStatus())
                .code(fermiCode.getCode())
                .message(fermiCode.getMessage())
                .build();
    }

    public String toJsonString() {
        return "{"
                + "\"status\":" + status + ","
                + "\"code\":\"" + code + "\","
                + "\"message\":\"" + message + "\""
                + "}";
    }
}
