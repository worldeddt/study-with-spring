package media.ftf.handler.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import media.ftf.enums.EUserType;
import media.ftf.enums.MemberNotificationType;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private MemberNotificationType state;
    private String roomId;
    private String userId;

    private EUserType userType;
    private String userName;
    private int tenantId;
    private String loginId;
    private DeviceInfo deviceInfo;
    private List<OptionDto> options;
}