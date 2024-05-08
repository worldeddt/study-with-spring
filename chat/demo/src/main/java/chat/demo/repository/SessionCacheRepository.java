package chat.demo.repository;


import chat.demo.repository.entity.SessionCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SessionCacheRepository extends CrudRepository<SessionCache, String> {

    SessionCache findByPrincipalName(String principalName);

    List<SessionCache> findAllByServer(String serverName);
}
