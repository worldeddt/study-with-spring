package chat.demo.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponse<T> {
    private HttpStatusCode headerStatusCode;
    private Integer bodyStatusCode;
    private T response;
}
