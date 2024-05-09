package media.ftf.module;

import lombok.Builder;
import lombok.Getter;
import media.ftf.enums.TicketType;

import java.security.Principal;



@Getter
@Builder
public class SessionInfo {
    private final Principal principal;
    private final String userId;
    private final String username;
    private final String roomId;
    private final TicketType ticketType;

    public static boolean isValid(SessionInfo sessionInfo) {
        return sessionInfo != null
                && sessionInfo.ticketType != null
                && sessionInfo.userId != null
                && (TicketType.MONITOR.equals(sessionInfo.getTicketType()) ||
                (TicketType.CONFERENCE.equals(sessionInfo.getTicketType()) &&
                        sessionInfo.roomId != null));
    }

}
