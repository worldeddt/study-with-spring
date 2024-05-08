package chat.demo.repository.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_invite_key")
public class InviteKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String inviteKey;

    private String hostId;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @PrePersist
    private void init() {
        createAt = LocalDateTime.now();
    }
}
