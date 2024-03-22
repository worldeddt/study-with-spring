package aop.prototypes.rtcOutBound.as.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DeviceInfo {
    private String os;
    private String browser;
}
