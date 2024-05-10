package chat.demo.repository;

import chat.demo.repository.dao.CallEntityDao;
import chat.demo.repository.entity.Call;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallEntityRepository extends JpaRepository<Call, Integer>, CallEntityDao {
}
