package chat.demo.application.dto;


import lombok.Getter;

@Getter
public class AcceptCallMessage {
    private String inviteKey;
    private boolean isAccept;
}
