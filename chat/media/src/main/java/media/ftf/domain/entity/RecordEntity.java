package media.ftf.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import media.ftf.enums.RecordStatus;
import media.ftf.enums.RecordType;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_room_record")
@Entity
public class RecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "roomId", nullable = false)
    private RoomEntity room;

    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "fileName", unique = true, nullable = false)
    private String fileName;

    @Column(name = "filePath")
    private String filePath;

    @Column(name = "orgSuffix", nullable = false)
    private String orgSuffix;

    @Column(name = "fileSize", nullable = false)
    @ColumnDefault("0")
    private long fileSize;

    @CreatedDate
    @Column(name = "createDate", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "type")
    private RecordType type;

    @Column(name = "status")
    private RecordStatus status;

}