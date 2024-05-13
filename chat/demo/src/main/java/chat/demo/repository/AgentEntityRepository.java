package chat.demo.repository;


import chat.demo.enums.AgentStatus;
import chat.demo.repository.dao.AgentEntityDao;
import chat.demo.repository.entity.Agent;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;

public interface AgentEntityRepository extends JpaRepository<Agent, String>, AgentEntityDao {

    // LockMode 로 데드락 걸릴 수 있으므로 함부로 사용하지 말 것
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Agent findByUserId(String userId);

    void deleteAllByServer(String serverName);

    Agent findByCallId(String callId);

    List<Agent> findAllByAgentStatus(AgentStatus agentStatus);
}
