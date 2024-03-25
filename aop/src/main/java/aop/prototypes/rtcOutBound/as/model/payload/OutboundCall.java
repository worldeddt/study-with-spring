package aop.prototypes.rtcOutBound.as.model.payload;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OutboundCall {
    private List<OptionDto> option;
    private long endDatetime; // 20231213103259, 기본 5분;
    private String categoryId;
    private String groupId;
}
