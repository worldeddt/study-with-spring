package chat.demo.repository.entity;


import chat.demo.enums.AgentStatus;
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
public class Agent {

    @Id
    private String userId;

    private String server;
    private String groupId;
    private String callId;
    private AgentStatus agentStatus;
}
