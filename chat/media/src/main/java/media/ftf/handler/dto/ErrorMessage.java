package media.ftf.handler.dto;

import com.fermi.multimedia.core.exception.FermiCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

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

    public static ErrorMessage of(FermiCode fermiCode) {
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
