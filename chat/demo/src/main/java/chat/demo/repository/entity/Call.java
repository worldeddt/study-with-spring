package chat.demo.repository.entity;


import chat.demo.enums.CallClosedReason;
import chat.demo.enums.CallType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @Column(name = "owner", nullable = false)
    private String owner;

    @Column(name = "multiMediaServer")
    private String multiMediaServer;

    @Column(name = "mediaServer")
    private String mediaServer;

    @Column(name = "closeReason")
    private CallClosedReason closeReason;

    @CreatedDate
    @Column(name = "createDate", nullable = false, updatable = false)
    private LocalDateTime createDate; // 콜 요청 시간

    @Column(name = "startDate")
    private LocalDateTime startDate; // 콜 시작 시간

    @Column(name = "endDate")
    private LocalDateTime endDate; // 콜 종료 시간


    @PrePersist
    public void init() {
        createDate = LocalDateTime.now();
    }
}
