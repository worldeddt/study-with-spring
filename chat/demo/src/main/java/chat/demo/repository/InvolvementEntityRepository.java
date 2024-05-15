package chat.demo.repository;

import chat.demo.repository.dao.InvolvementEntityDao;
import chat.demo.repository.entity.Involvement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvolvementEntityRepository extends JpaRepository<Involvement, Long> {

}
