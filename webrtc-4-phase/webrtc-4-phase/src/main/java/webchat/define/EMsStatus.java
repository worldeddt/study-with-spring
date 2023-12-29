package webchat.define;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum EMsStatus {
    UR("unregistered"),
    CD("Connected"),
    DC("Disconnected");

    private String msg;
}
