package aop.prototypes.redis.templateExample.controller;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@MessageMapping("/call")
public class TemplateController {
}
