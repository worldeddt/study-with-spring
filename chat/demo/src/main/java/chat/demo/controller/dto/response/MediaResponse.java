package chat.demo.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MediaResponse<T> {
    private int status;
    private String message;
    private T data;
}
