package web.coviewpractice.repository;


import org.springframework.data.repository.CrudRepository;
import web.coviewpractice.common.data.AgentStatusCache;
import web.coviewpractice.common.enums.AgentStatus;

import java.util.List;

public interface AgentRedisRepository extends CrudRepository<AgentStatusCache, String> {

    AgentStatusCache findByAgentStatus(AgentStatus agentStatus);

    List<AgentStatusCache> findAllByAgentStatus(AgentStatus agentStatus);

    List<AgentStatusCache> findAllByUserName(String userName);

    List<AgentStatusCache> findAllByIsOwner(boolean isOwner);
}
