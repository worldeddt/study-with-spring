package chat.demo.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisPubType {

    USER(1, "유저에게만 전송"),
    CALL(2, "call 참여자 전체 전송");

    private final int code;
    private final String msg;
}
