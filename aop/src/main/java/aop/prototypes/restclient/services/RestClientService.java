package aop.prototypes.restclient.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.function.Consumer;



@Slf4j
@RestController
public class RestClientService {


    @PostMapping("/rest/post")
    public void restPost() {
        RestClient build = RestClient.builder()
                .baseUrl("https://localhost/call/api/get/agent")
                .build();

        build.post()
                .contentType(
                        MediaType.APPLICATION_JSON
                ).header(
                HttpHeaders.AUTHORIZATION, "Bearer test"
        ).body("{}").exchange((request, response) -> {
            log.info("response : {}", response);
            return response;
        });
    }
}
