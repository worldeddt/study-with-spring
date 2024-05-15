package chat.demo.repository.entity;

import chat.demo.enums.CallType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;


@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_call_invite_key")
@Entity
public class InviteKey {
    @Id
    @Column(name = "inviteKey", unique = true, nullable = false)
    private String key;

    @Column(name = "server", nullable = false)
    private String server;

    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "callType", nullable = false)
    private CallType callType;

    @Column(name = "destnId")
    private String destnId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "callId", nullable = false, updatable = false)
    private Call call;

    @Column(name = "expiredDate")
    private LocalDateTime expiredDate;

    @Column(name = "isUsed")
    private Boolean isUsed;

}
