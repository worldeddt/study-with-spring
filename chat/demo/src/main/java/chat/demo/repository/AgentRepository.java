package chat.demo.repository;


import chat.demo.repository.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AgentRepository extends JpaRepository<Agent, String> {
}
