package aop.prototypes.kurentoMultiInstance.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum KurentoStatus {
    CONNECTED("연결됨"),
    CONNECTING("연결중"),
    CONNECTIONFAIL("연결 실패");

    private final String description;
}
