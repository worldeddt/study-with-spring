package media.ftf.repository;

import media.ftf.domain.entity.RecordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<RecordEntity, Long> {

    Optional<List<RecordEntity>> findAllByRoomId(String roomId);

    Page<RecordEntity> findAllByRoomId(String roomId, Pageable pageable);

    Optional<RecordEntity> findOneByFileName(String fileName);

}
