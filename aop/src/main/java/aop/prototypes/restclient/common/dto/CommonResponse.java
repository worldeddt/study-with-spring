package aop.prototypes.restclient.common.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class CommonResponse<T> {

    private int resultCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;


    public static CommonResponse of(
            String reserveId
    ) {
        return CommonResponse.builder()
                .reserveId(reserveId)
                .build();
    }
}
