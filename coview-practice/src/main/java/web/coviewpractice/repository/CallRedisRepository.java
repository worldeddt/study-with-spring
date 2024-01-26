package web.coviewpractice.repository;


import org.springframework.data.repository.CrudRepository;
import web.coviewpractice.common.data.CallKeyCache;

public interface CallRedisRepository extends CrudRepository<CallKeyCache, String> {

}
