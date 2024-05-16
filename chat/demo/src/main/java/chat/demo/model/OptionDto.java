package chat.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class OptionDto {
    private String key;
    private String value;

    public boolean isValid() {
        return key != null && value != null;
    }
}