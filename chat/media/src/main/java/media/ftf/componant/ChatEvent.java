package media.ftf.componant;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import media.ftf.handler.dto.ChatMessage;

@Getter
@ToString
@Builder
public class ChatEvent {

    private String roomId;
    private ChatMessage chatMessage;
}
