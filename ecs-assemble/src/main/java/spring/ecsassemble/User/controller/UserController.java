package spring.ecsassemble.User.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/v1/user")
public class UserController {

    @GetMapping("/whoami")
    public String answer() {
        return "ecs assemble!";
    }
}
