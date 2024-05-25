package media.ftf.handler.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class OptionDto {
    private String key;
    private String value;
}
