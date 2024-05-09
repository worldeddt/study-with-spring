package media.ftf.repository;


import media.ftf.domain.entity.RoomEntity;
import media.ftf.repository.dao.RoomDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomEntity, String>, RoomDao {
}

