package chat.demo.model;

import chat.demo.enums.EUserType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.security.Principal;

@EqualsAndHashCode
@ToString
@Getter
@Builder
public class SessionInfo {

    private final Principal principal;
    private final String userId;
    private final String userName;
    private final EUserType userType;
    private final String loginId;

}