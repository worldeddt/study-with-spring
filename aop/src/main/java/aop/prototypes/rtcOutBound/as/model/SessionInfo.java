package aop.prototypes.rtcOutBound.as.model;

import aop.prototypes.rtcOutBound.as.common.enums.EUserType;
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
    private final int tenantId;
    private final String loginId;
    private final DeviceInfo deviceInfo;
    private final boolean isAgent;
    private final String token;

}
