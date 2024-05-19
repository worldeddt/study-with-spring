package chat.demo.controller.dto;


import chat.demo.controller.dto.response.Ticket;
import chat.demo.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TicketMessage {
    private final NotificationType type = NotificationType.TICKET;
    private Ticket ticket;
}
