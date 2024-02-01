package aop.prototypes.aop.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import aop.prototypes.webclient.services.WebClientService;

@RestController
@RequiredArgsConstructor
public class WebClientController {

    private final WebClientService webClientService;

    @GetMapping("/trigger")
    private String trigger() {
        webClientService.init();
        return "";
    }

    @GetMapping("/get")
    private String getMethod() {
        return "getMethod";
    }
}
