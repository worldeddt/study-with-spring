package aop.prototypes.rtcOutBound.as.model.payload;

import aop.prototypes.rtcOutBound.as.common.enums.CallType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
public class CallMessage {

    @NotNull
    private CallType callType;

    private List<OptionDto> option;

    // 3자 초대
    private String helperId;

    private String categoryId;

    // inbound
    private String groupId;

    private long expireTime = 5 * 60L; // ttl 시간 // 5분

    public OutboundCall getOutboundCall() {
        return OutboundCall.builder()
                .categoryId(categoryId)
                .groupId(groupId)
                .option(option)
                .endDatetime(expireTime)
                .build();
    }

}