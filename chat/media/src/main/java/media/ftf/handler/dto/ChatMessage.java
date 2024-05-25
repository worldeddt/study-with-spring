package media.ftf.handler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import media.ftf.enums.EMsgType;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private EMsgType type;
    private String sender;
    private String receiver;
    private String message;
    private LocalDateTime date = LocalDateTime.now();
}