package media.ftf.application.dto.request;



import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import media.ftf.enums.EUserType;

import java.util.List;

@ToString
@Getter
@Builder
public class TokenRequest {

    private String roomId;
    private String userId;
    private EUserType userType;

    // 부가 정보
    private String userName;
    private int tenantId;
    private String loginId;
    private DeviceInfo deviceInfo;
    private List<OptionDto> option;
}
