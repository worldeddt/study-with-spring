package chat.demo.repository;

import chat.demo.repository.dao.OptionEntityDao;
import chat.demo.repository.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionEntityRepository extends JpaRepository<Option, Long>, OptionEntityDao {
    List<Option> findAllByParticipantId(long participantId);
}
