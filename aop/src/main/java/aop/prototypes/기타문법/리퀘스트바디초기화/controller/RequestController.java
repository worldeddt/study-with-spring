package aop.prototypes.기타문법.리퀘스트바디초기화.controller;


import aop.prototypes.기타문법.리퀘스트바디초기화.controller.dto.RequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RestController
public class RequestController {


    @PostMapping("/id")
    public void request1(@RequestBody RequestDto requestDto) {
        log.info("request dto : {}", requestDto.getId());
    }
}
