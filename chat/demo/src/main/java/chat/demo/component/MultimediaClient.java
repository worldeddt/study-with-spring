package chat.demo.component;


import chat.demo.controller.dto.request.RoomRequest;
import chat.demo.controller.dto.response.ClientResponse;
import chat.demo.controller.dto.response.CreateRoomResponse;
import chat.demo.controller.dto.response.MediaResponse;
import chat.demo.properties.ChatProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class MultimediaClient {

    private final ChatProperties chatProperties;
    private final RestTemplate restTemplate;

    public ClientResponse<CreateRoomResponse> createRoom(RoomRequest roomRequest) {

        String entryPoint = chatProperties.httpClient().server().multimedia().entryPoint();

        final var method = HttpMethod.POST;
        final var url = entryPoint + "/api/post/room";

        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.remove("expires");
        headers.remove("cache-control");

        final var returnType = new ParameterizedTypeReference<MediaResponse<CreateRoomResponse>>() {
        };
        final var resp =
                restTemplate.exchange(url, method, new HttpEntity<>(roomRequest, headers), returnType);

        final var body = resp.getBody();
        final var bodyStatusCode = body == null ? null : body.getStatus();
        final var createRoomResponse = resp.getBody() == null ? null : resp.getBody().getData();

        return new ClientResponse<>(resp.getStatusCode(), bodyStatusCode, createRoomResponse);

    }
}
