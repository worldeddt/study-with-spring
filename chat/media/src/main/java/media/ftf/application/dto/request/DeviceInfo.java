package media.ftf.application.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DeviceInfo {
    private String os;
    private String browser;
}
