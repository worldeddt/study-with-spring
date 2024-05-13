package chat.demo.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_call_option")
@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "participantId", nullable = false, updatable = false)
    private Participant participant;

    @Column(name = "optionName", nullable = false, updatable = false)
    private String optionName;

    @Column(name = "optionValue", nullable = false, updatable = false)
    private String optionValue;
}
