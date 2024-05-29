package media.ftf.dto.response;

import lombok.Builder;
import lombok.Getter;
import media.ftf.enums.CommonCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@Getter
@Builder
public class CommonResponse<T> {

    private int status;
    private String message;
    private T data;

    public static <T> CommonResponse<T> ok(T data) {
        return new CommonResponse<>(HttpStatus.OK.value(), "OK", data);
    }

    public static <T> CommonResponse<T> ok() {
        return ok(null);
    }

    public static CommonResponse<Void> of(int statusCode, String message) {
        return new CommonResponse<>(statusCode, message, null);
    }

    public static <T> CommonResponse<T> of(CommonCode commonCode, T messages) {
        return new CommonResponse<>(commonCode.getStatus(), commonCode.getMessage(), messages);
    }

    public static CommonResponse<Void> of(CommonCode code) {
        return new CommonResponse<>(code.getStatus(), code.getMessage(), null);
    }


    public static CommonResponse<List<String>> of(CommonCode commonCode, MethodArgumentTypeMismatchException e) {
        return new CommonResponse<>(commonCode.getStatus(), commonCode.getMessage(), List.of(getResultMessage(e.getName(), e.getValue(), e.getErrorCode())));
    }

    public static CommonResponse<List<String>> of(CommonCode commonCode, List<FieldError> errorList) {
        final var list = errorList.stream().map(CommonResponse::getResultMessage).toList();
        return new CommonResponse<>(commonCode.getStatus(), commonCode.getMessage(), list);
    }

    public static String getResultMessage(FieldError fieldError) {
        final var field = fieldError.getField();
        final var rejectedValue = fieldError.getRejectedValue();
        final var defaultMessage = fieldError.getDefaultMessage();
        return getResultMessage(field, rejectedValue, defaultMessage);
    }

    private static String getResultMessage(String field, Object rejectedValue, String defaultMessage) {
        return String.format("field: %s, value: %s, message: %s", field, rejectedValue, defaultMessage);
    }
}
