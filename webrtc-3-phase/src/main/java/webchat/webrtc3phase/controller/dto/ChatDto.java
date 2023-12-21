package webchat.webrtc3phase.controller.dto;


import lombok.*;

import java.util.PrimitiveIterator;

@Getter
@Setter
@Builder
public class ChatDto {

    private MessageType messageType;
    private String roomId;
    private String sender;
    private String message;
    private String time;

    /* 파일 업로드 관련 변수 */
    private String s3DataUrl; // 파일 업로드 url
    private String fileName; // 파일이름
    private String fileDir; // s3 파일 경로

    public enum MessageType {
        ENTER, TALK, LEAVE;
    }
}
