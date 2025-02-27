package spring.rediscluster.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // set이 아니기에 키가 존재하면 값을 overwrite 함.
    public void addKey(String key, String value){
        try{
            log.info("haskey : {}, boolean: {}", key , redisTemplate.hasKey(key));

            if(redisTemplate.hasKey(key)) redisTemplate.delete(key);

            redisTemplate.opsForValue().set(key, value);
        }catch (Exception e){
            log.error("### Redis Set Key Error !!! ::: {}/{}", e.getStackTrace(), e.getCause());
        }
    }

    public void addHash(String person, String key, String value) {
        try {
            redisTemplate.opsForHash().put(person, key, value);
        } catch ( Exception e) {
            log.error("### Redis set hash memory Error !! {}", e.getMessage());
        }
    }

    public String getValue(String key){
        String value = "";
        try{
            if(redisTemplate.hasKey(key)){
                value = redisTemplate.opsForValue().get(key);
            }
        }catch (Exception e){
            log.error("### Redis Set Key Error !!! ::: {}", e.getMessage());
        }
        return value;
    }
}
