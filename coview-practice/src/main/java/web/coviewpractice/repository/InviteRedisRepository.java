package web.coviewpractice.repository;


import org.springframework.data.repository.CrudRepository;
import web.coviewpractice.common.data.InviteKeyCache;

import java.util.List;

public interface InviteRedisRepository extends CrudRepository<InviteKeyCache, String> {

    List<InviteKeyCache> findAllByUserId(String userId);

}
