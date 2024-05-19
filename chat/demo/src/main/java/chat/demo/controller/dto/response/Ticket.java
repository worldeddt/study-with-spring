package chat.demo.controller.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Ticket {
    private String multiMediaServer;
    private String mediaServer;
    private String roomId;
}
