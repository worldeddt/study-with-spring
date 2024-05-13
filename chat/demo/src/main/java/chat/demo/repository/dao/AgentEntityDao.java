package chat.demo.repository.dao;


import chat.demo.enums.AgentStatus;

import java.util.List;

public interface AgentEntityDao {

    long updateAgentStatus(String userId, AgentStatus agentStatus);

    long updateAgentStatus(String userId, String callId, AgentStatus agentStatus);

    long updateAgentStatusToRG(String userId);

    long updateAgentStatusToRgWithCallId(String userId, String callId);

    long updateAgentStatusToOR(String userId);

    long updateAgentStatusToOrWithCallId(String userId, String callId);

    long updateAgentStatusToIR(String userId);

    long updateAgentStatusToIrWithCallId(String userId, String callId);

    long updateAgentStatusToCI(String userId);

    long updateAgentStatusToCiWithCallId(String userId, String callId);

    long updateAgentStatusToIA(String userId);

    long updateAgentStatusToAS(String userId);

    long updateAgentStatusToUR(String userId);

    long updateAgentStatusToUrWithServerName(String serverName);
}
