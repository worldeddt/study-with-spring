package aop.prototypes.restclient.config;


import aop.prototypes.restclient.common.dto.CommonResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Configuration
public class RestClientModule {

    @Bean
    public RestClient registerRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8090/v1/proto")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public synchronized CommonResponse<?> get(String uri) {
        RestClient restClient = registerRestClient();

        return restClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange( (request, response) -> {
                    if (response.getStatusCode().is4xxClientError() ||
                            response.getStatusCode().is5xxServerError()) {
                        return new CommonResponse<>(
                                response.getStatusCode().value(),
                                null
                        );
                    }

                    return Objects.requireNonNull(response.bodyTo(CommonResponse.class));
                });
    }
}
