package chat.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;


@Getter
@AllArgsConstructor
public enum AgentStatus {
    RG("Registered", "대기중"), // 대기
    OR("Outbound Reservation", "아웃바운드 배정받음"),
    IR("Inbound Reservation", "인바운드 배정받음"), // 배정 (RV : 아웃바운드 / CR : 인바운드)
    CI("Counseling", "상담중"), // 상담
    IA("Inactive", "휴식중"), // ETC
    AS("After Service", "후처리중"), // 후처리
    UR("Unregistered", "상담불가"); // 상담불가 (로그아웃)

    private final String value;
    private final String description;
}
