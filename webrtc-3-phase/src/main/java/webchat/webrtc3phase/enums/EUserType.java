package webchat.webrtc3phase.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EUserType {

    TEST("TestUser"),
    //SCREEN("Screen"),	// For Agent Canvas Stream.
    SIPCLIENT("SIP Client"),
    CLIENT("Client"),
    STAFF("Agent"),
    MANAGER("Manager"),
    AADMIN("Agent Administrator"),    // 상담 모니터링 가능.
    TADMIN("Tenant Administrator"),   // 상담 모니터링 가능.
    PADMIN("Provider Administrator"), // 상담 모니터링 가능.
    // 221019 Unknown 유저타입 추가
    UNKNOWN("Unknown"); // 상담 모니터링 가능.


    private String msg;
}

