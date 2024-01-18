package spring.rediscluster.controller;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spring.rediscluster.controller.dto.RedisKey;
import spring.rediscluster.services.RedisService;
import spring.vo.RedisInfo;

@Slf4j
@RestController
public class RedisController {

    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json; charset=utf8")
    public Object register(@RequestBody RedisInfo redisInfo){
        redisService.addKey(redisInfo.getKey(), redisInfo.getValue());
        return redisInfo;
    }

    @PostMapping(value = "/find", produces = "application/json; charset=utf8")
    public Object get(@RequestBody RedisKey redisKey){
        log.info("redis : {}", redisKey.getKey());
        RedisInfo redisInfo = new RedisInfo();
        redisInfo.setKey(redisKey.getKey());
        redisInfo.setValue(redisService.getValue(redisKey.getKey()));
        return redisInfo;
    }
}
