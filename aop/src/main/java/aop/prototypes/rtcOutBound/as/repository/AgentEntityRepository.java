package aop.prototypes.rtcOutBound.as.repository;

import aop.prototypes.rtcOutBound.as.entities.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentEntityRepository extends JpaRepository<Agent, Integer> {

}
