package chat.demo.repository;


import chat.demo.repository.entity.SessionCache;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SessionCacheRepository extends CrudRepository<SessionCache, String> {
    SessionCache findByPrincipalName(String principalName);

    List<SessionCache> findAllByServer(String serverName);
}
