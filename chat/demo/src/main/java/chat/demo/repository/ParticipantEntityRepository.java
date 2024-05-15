package chat.demo.repository;

import chat.demo.repository.dao.ParticipantEntityDao;
import chat.demo.repository.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantEntityRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByCallId(String callId);
}
