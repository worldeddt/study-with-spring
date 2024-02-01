package aop.prototypes.webclient.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.atomic.AtomicReference;


@Slf4j
@Service
public class WebClientService {

    public String init() {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/get")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        return nonBlock(webClient);
    }

    private String nonBlock(WebClient webClient) {
        AtomicReference<String> response =
                new AtomicReference<>("");
        webClient.get()
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(e -> {
                    response.set(e);
                });

        log.info("non block result :{}",response.get());
        return response.get();
    }
}
