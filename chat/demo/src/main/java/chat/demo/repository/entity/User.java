package chat.demo.repository.entity;

import chat.demo.enums.UserStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_agent_status")
@Entity
public class User {

    @Id
    private String userId;

    private String username;

    private String server;

    private String callId;

    private UserStatus userStatus;

}
