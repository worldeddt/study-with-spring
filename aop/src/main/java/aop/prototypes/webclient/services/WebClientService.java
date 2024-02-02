package aop.prototypes.webclient.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;


@Slf4j
@Service
public class WebClientService {

    public String init() {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        return nonBlock(webClient);
    }

    private String block(WebClient webClient) {
        String block = webClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    log.info("response : {}", response.bodyToMono(String.class).block());
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(String.class);
                    } else {
                        return response.createError();
                    }
                }).block();
        return block;
    }

    private String nonBlock(WebClient webClient) {
        AtomicReference<String> result = new AtomicReference<>("");

        Mono<String> stringMono = webClient.get()
                .uri("/get")
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(WebClientService::handleError);

        stringMono.subscribe(
                response -> {
                    log.info("response : {}", response);
                    result.set(response);
                },
                throwable -> handleError(throwable)
        );

        // 결과를 받아오기 전에 쓰레드가 종료되는 것을 방지한다.
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result.get();
    }

    private static void handleError(Throwable throwable) {
        // 여기에서 예외 처리 로직을 작성합니다.
        System.err.println("Error occurred: " + throwable.getMessage());
    }
}
