package chat.demo.repository.entity;

import chat.demo.enums.EUserType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_call_participant")
@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "callId", nullable = false, updatable = false)
    private Call call;

    @Column(name = "userType", nullable = false, updatable = false)
    private EUserType userType;

    @Column(name = "userId", nullable = false, updatable = false)
    private String userId;

    @Column(name = "userName")
    private String userName;

    @Column(name = "loginId")
    private String loginId;

    @Column(name = "userOS")
    private String userOS;

    @Column(name = "userBrowser")
    private String userBrowser;

    @CreatedDate
    @Column(name = "createDate", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "participant")
    private final List<Option> options = new ArrayList<>();
}
