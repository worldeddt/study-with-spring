package chat.demo.controller.dto;


import chat.demo.enums.RedisPubType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RedisPubDto {
    private RedisPubType redisPubType;
    private String userId;
    private String callId;
    private Object obj;
}
