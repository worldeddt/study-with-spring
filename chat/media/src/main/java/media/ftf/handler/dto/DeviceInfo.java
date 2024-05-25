package media.ftf.handler.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@ToString
@Builder
@Data
public class DeviceInfo {
    private String os;
    private String browser;
}
