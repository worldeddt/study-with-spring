package media.ftf.module;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import media.ftf.enums.EUserType;
import media.ftf.enums.TicketType;
import media.ftf.handler.dto.DeviceInfo;
import media.ftf.handler.dto.OptionDto;

import java.security.Principal;
import java.util.List;


@ToString
@Getter
@Builder(toBuilder = true)
public class SessionInfo {

    private final Principal principal;

    private final TicketType ticketType;
    private final String roomId;

    private final EUserType userType;
    private final String userId;
    private final String userName;

    private final int tenantId;
    private final String loginId;
    private final DeviceInfo deviceInfo;
    private final List<OptionDto> options;

    private final boolean isAgent;

    public static boolean isValid(SessionInfo sessionInfo) {
        return sessionInfo != null
                && sessionInfo.ticketType != null
                && sessionInfo.userId != null
                && (TicketType.MONITOR.equals(sessionInfo.getTicketType()) || (TicketType.CONFERENCE.equals(sessionInfo.getTicketType()) && sessionInfo.roomId != null));
    }

}
