package aop.prototypes.logBack.controller;


import aop.prototypes.logBack.services.LogBackService;
import aop.prototypes.logBack.services.LogBackServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogBackController {
    private final LogBackService logBackService;

    public LogBackController(LogBackServiceImpl logBackService) {
        this.logBackService = new LogBackServiceImpl();
    }

    @PostMapping(value = "/log/back")
    public void log() {
        this.logBackService.loggingMaster();
    }
}
