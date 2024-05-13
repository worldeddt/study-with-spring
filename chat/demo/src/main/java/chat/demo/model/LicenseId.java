package chat.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class LicenseId implements Serializable { // 복합키를 위한 클래스
    @Column(name = "tenantId")
    private Integer tenantId;

    @Column(name = "server")
    private String server;
}
