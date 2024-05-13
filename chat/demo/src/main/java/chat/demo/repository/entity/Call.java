package chat.demo.repository.entity;



import chat.demo.enums.CallClosedReason;
import chat.demo.enums.CallType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_call")
@Entity
public class Call {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "callType", nullable = false)
    private CallType callType;

    @Column(name = "caller", nullable = false)
    private String caller;

    @Column(name = "owner")
    private String owner;

    @Column(name = "multiMediaServer")
    private String multiMediaServer;

    @Column(name = "mediaServer")
    private String mediaServer;

    @Column(name = "groupId")
    private String groupId;

    @Column(name = "categoryId")
    private String categoryId;

    @Column(name = "callServer")
    private String callServer;

    @Column(name = "closeReason")
    private CallClosedReason closeReason;

    @CreatedDate
    @Column(name = "createDate", nullable = false, updatable = false)
    private LocalDateTime createDate; // 콜 요청 시간

    @Column(name = "startDate")
    private LocalDateTime startDate; // 콜 시작 시간

    @Column(name = "endDate")
    private LocalDateTime endDate; // 콜 종료 시간
}
