package chat.demo.repository.entity;

import chat.demo.enums.InvolvementType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_call_agent_history")
@Entity
public class Involvement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "callId", nullable = false, updatable = false)
    private Call call;

    @Column(name = "agentId", nullable = false, updatable = false)
    private String agentId;

    @Column(name = "type", nullable = false, updatable = false)
    private InvolvementType type;

    @CreatedDate
    @Column(name = "createDate", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @PrePersist
    public void init() {
        createDate = LocalDateTime.now();
    }

}
