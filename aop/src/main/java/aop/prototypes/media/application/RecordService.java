package aop.prototypes.media.application;


import aop.prototypes.media.common.config.KurentoConfig;
import aop.prototypes.redis.cache.entities.request.ParameterObject;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.WebRtcEndpoint;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RecordService {

    @Getter
    static class RecordBody {
        private String key;
        private String value;
    }

    private final KurentoConfig kurentoConfig;
    private final RedisTemplate<String , String> redisForMessage;


    @GetMapping("/record/{key}")
    public void record(@PathVariable("key") String key) {
        log.info("{}", redisForMessage.opsForValue().get(key));
    }


    @PostMapping("/record")
    public void record(@RequestBody RecordBody recordBody) {

        redisForMessage.opsForValue().set(recordBody.getKey(), recordBody.getValue());

        log.info("success");
    }
}
