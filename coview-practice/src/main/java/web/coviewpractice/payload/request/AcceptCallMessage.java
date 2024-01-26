package web.coviewpractice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@ToString
@Getter
@AllArgsConstructor
public class AcceptCallMessage {
    private String inviteKey;
    private boolean isAccept;
    private String endDatetime;
    // 고객 -> 서버 : outbound [][]
    // 서버 -> 매니저 -> 서버 : help [][]
    // 서버 -> 매니저 -> 서버 : inbound [][]
}
