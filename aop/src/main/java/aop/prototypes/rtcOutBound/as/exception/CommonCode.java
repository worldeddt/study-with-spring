package aop.prototypes.rtcOutBound.as.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
public enum CommonCode {

    SUCCESS(HttpStatus.OK, "P001", "요청에 성공하였습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "P002", "대상을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "P003", "서버 오류가 발생했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "P004", "잘못된 요청입니다."),
    AUTHORIZATION_ERROR(HttpStatus.BAD_REQUEST, "P005", "인증 오류입니다."),
    NOT_AGENT(HttpStatus.FORBIDDEN, "C-022", "Only agent can access");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
