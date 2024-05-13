package chat.demo.repository;

import chat.demo.repository.dao.InviteKeyEntityDao;
import chat.demo.repository.entity.InviteKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InviteKeyEntityRepository extends JpaRepository<InviteKey, String>, InviteKeyEntityDao {
    List<InviteKey> findAllByCallId(String callId);
}
