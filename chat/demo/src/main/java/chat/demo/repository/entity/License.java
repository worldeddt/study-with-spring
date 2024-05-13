package chat.demo.repository.entity;


import chat.demo.model.LicenseId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_call_license")
@Entity
public class License {
    @EmbeddedId // 복합키
    private LicenseId licenseId;

    @Column(name = "currentCallCount")
    private Long currentCallCount;

    @Column(name = "maxCallCount")
    private Long maxCallCount;
}
