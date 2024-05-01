package chat.demo.repository;


import chat.demo.repository.domain.SessionCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionCacheRepository extends CrudRepository<SessionCache, String> {
}
