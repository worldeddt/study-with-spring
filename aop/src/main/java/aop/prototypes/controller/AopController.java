package aop.prototypes.controller;


import aop.prototypes.controller.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@Slf4j
@RestController
public class AopController {
    @GetMapping("/aop")
    public String aopGet() {
        return "생성자 실행";
    }

    @PostMapping(value = "/user",produces = "application/json; charset=UTF-8")
    public User postMethod(@RequestBody User user) {
        log.info("post method 실행 ");
        return user;
    }

    @PutMapping(value = "/user", produces = "application/json; charset=UTF-8")
    public User putMethod(@RequestBody User user) {
        log.info("put method 실행 ");
        return user;
    }
}
