package spring.rediscluster.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import spring.rediscluster.services.RedisService;
import spring.vo.RedisInfo;

@Controller
public class RedisController {

    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json; charset=utf8")
    public Object register(@RequestBody RedisInfo redisInfo){
        redisService.addKey(redisInfo.getKey(), redisInfo.getValue());
        return redisInfo;
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST, produces = "application/json; charset=utf8")
    public Object get(@RequestBody RedisInfo redisInfo){
        String value = redisService.getValue(redisInfo.getKey());
        redisInfo.setValue(value);
        return redisInfo;
    }
}
