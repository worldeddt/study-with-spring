package chat.demo.repository.entity;


import chat.demo.enums.CallType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_invite_key")
public class InviteKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String inviteKey;

    private String hostId;

    private String serverName;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "callId", nullable = false, updatable = false)
    private Call call;

    private Boolean isUsed;
    private String destnId;

    @CreatedDate
    @Column(name = "createDate", nullable = false, updatable = false)
    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private CallType callType;
}
