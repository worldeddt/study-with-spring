package media.ftf.application.dto;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OptionDto {
    private String key;
    private String value;
}

