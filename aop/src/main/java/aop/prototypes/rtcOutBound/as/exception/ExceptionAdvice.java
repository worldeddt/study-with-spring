package aop.prototypes.rtcOutBound.as.exception;


import aop.prototypes.rtcOutBound.as.common.response.CommonResponse;
import ch.qos.logback.core.model.processor.ModelHandlerException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.ForwardedHeaderUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.data.redis.connection.ReactiveStreamCommands.AddStreamRecord.body;


@Slf4j
@RestControllerAdvice(annotations = RestController.class)
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({CommonException.class})
    public ResponseEntity<CommonResponse<?>> handleCommonException(CommonException e, HttpServletRequest request) {
        logException(e.getCommonCode().getStatus().is5xxServerError()? Level.ERROR : Level.INFO, e, request);

        return ResponseEntity.status(e.getCommonCode().getStatus()).body(new CommonResponse<>(e.getCommonCode().getStatus()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponse<?>> handleConstraintViolationException(
            ConstraintViolationException e, HttpServletRequest request) {
        this.logException(Level.INFO, e, request);

        CommonResponse<?> response = new CommonResponse<>(
                HttpStatus.BAD_REQUEST.value(), e.getMessage(),
                System.currentTimeMillis(), Optional.ofNullable(null));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<CommonResponse<?>> handleBindException(BindException e, HttpServletRequest request) {
        this.logException(Level.INFO, e, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResponse<>(CommonCode.BAD_REQUEST));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException e, HttpServletRequest request) {
        this.logException(Level.INFO, e, request);

        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> (error.getField() + ": " +
                        Objects.requireNonNullElse(error.getDefaultMessage(), "")).trim()).toList();
        CommonResponse<?> response = new CommonResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                StringUtils.join(errors, ','),
                System.currentTimeMillis(),
                Optional.ofNullable(null)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ModelHandlerException.class)
    public ResponseEntity<CommonResponse<?>> handlerCommonException(CommonException e, HttpServletRequest request) {
        logException(e.getCommonCode().getStatus().is5xxServerError()? Level.ERROR : Level.INFO, e, request);

        return ResponseEntity.status(e.getCommonCode().getStatus()).body(new CommonResponse<>(e.getCommonCode().getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<?>> handleException(Exception e, HttpServletRequest request) {
        logException(Level.ERROR, e, request);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponse<>(CommonCode.INTERNAL_SERVER_ERROR));
    }


    private void logException(Level level, Exception e, HttpServletRequest request) {
        ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(request);

        String uriString = ForwardedHeaderUtils.adaptFromForwardedHeaders(
                servletServerHttpRequest.getURI(),
                servletServerHttpRequest.getHeaders()
        ).build().toUriString();

        log.atLevel(level).log("[{}] method: {} | url: {} | message: {}",
                e.getClass().getName(), request.getMethod(), uriString, e.getMessage());
    }

}
