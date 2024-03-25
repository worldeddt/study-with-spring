package aop.prototypes.rtcOutBound.as.common.response;


import aop.prototypes.rtcOutBound.as.exception.CommonCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class CommonResponse<T> {

    private int code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    private Long timestamp;

    public static CommonResponse<?> basic() {
        return CommonResponse.builder()
                .code(200)
                .message("success")
                .data(null)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public CommonResponse(int code, String message, Long timestamp, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    public CommonResponse(int code, String message, Long timestamp) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
    }

    public CommonResponse(T data) {
        this.code = CommonCode.SUCCESS.getStatus().value();
        this.message = CommonCode.SUCCESS.getMessage();
        this.timestamp = System.currentTimeMillis();
        this.data = data;
    }

    public CommonResponse(CommonCode status) {
        this.code = status.getStatus().value();
        this.message = status.getMessage();
        this.timestamp = System.currentTimeMillis();
        this.data = null;
    }

    public CommonResponse(T data, CommonCode status) {
        this.code = status.getStatus().value();
        this.message = status.getMessage();
        this.timestamp = System.currentTimeMillis();
        this.data = data;
    }
}
