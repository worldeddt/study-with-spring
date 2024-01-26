package web.coviewpractice.dto;

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
    private final String tenantId;
    private final String userId;
    private final boolean isAgent;
}

