package chat.demo.repository;

import chat.demo.model.LicenseId;
import chat.demo.repository.dao.LicenseEntityDao;
import chat.demo.repository.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseEntityRepository extends JpaRepository<License, LicenseId>, LicenseEntityDao {
}
