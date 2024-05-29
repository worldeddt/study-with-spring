package media.ftf.handler.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import media.ftf.enums.CommonCode;

import java.util.List;

@ToString
@Getter
@Builder
public class ErrorMessage {
    private int status;
    private String code;
    private String message;
    private String destination;
    private String payload;
    private List<String> data;

    public static ErrorMessage of(CommonCode commonCode) {
        return ErrorMessage.builder()
                .status(commonCode.getStatus())
                .code(commonCode.getCode())
                .message(commonCode.getMessage())
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
