package chat.demo.enums;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum CallClosedReason {
    // 판테온 ClosedReason 가져옴
    Completed(200, "Completed."),
    AgentDeferred(201, "AgentDeferred"), // 콜종료 버튼을 눌렀으나 상담사측 이슈로 임의 종료
    ClientDeferred(202, "ClientDeferred"), // 콜종료 버튼을 눌렀으나 고객측 이슈로 임의 종료
    Uncompleted(300, "Uncompleted."),
    ForcedLogout(301, "Forced Logout"), // 강제 로그아웃으로 인해 비정상 종료
    ClientCancel(400, "Client Canceled."), //인바운드 콜 시 고객이 상담취소 버튼 클릭
    AgentCancel(401, "Agent Canceled."), //아웃바운드 상담사 취소
    AgentReject(500, "Agent Rejected."), //인바운드 콜 시 상담사가 거절
    ClientReject(501, "Client Rejected."), //코드에는 있으나 로직상 사용하지 않음
    Timeout(600, "Connection Timeout."),  // 콜 시 상담사 무응답으로 시간 초과 or 아웃바운드 시 초대 후 시간 지남
    Error(700, "Unknown Error."),
    SystemDown(800, "System Down."),
    Busy(900, "Busy Status."),
    Flood(901, "Flood Status."),
    Nobody(903, "There's Nobody."),
    Rest(904, "Rest Status."),
    Holiday(905, "Holiday."),
    NABD(906, "Not A Business Day."),
    CtiQueueFail(1001, "고객 큐 대기 실패");

    private static final Map<Integer, String> codeIndex;

    @JsonValue
    private final int code;
    private final String msg;

    static {
        codeIndex = Arrays.stream(CallClosedReason.values())
                .collect(Collectors.toUnmodifiableMap(v -> v.code, Enum::name));
    }


    public static CallClosedReason convert(Integer code) {
        if (code == null) return null;
        if (!containsByValue(code)) return null;
        return CallClosedReason.valueOf(codeIndex.get(code));
    }

    public static boolean containsByValue(Integer code) {
        return code != null && codeIndex.containsKey(code);
    }
}

