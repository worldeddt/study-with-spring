package chat.demo.application.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class AcceptCallMessage {
    private String inviteKey;
    private boolean isAccept;
}
