package chat.demo.application.dto;


import chat.demo.enums.CallType;
import chat.demo.model.OptionDto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Setter
@Getter
public class RequestCallMessage {

    @NotNull
    private CallType callType;

    private List<OptionDto> options;

    // 3자 초대
    private String helperId;

    private String categoryId;

    // inbound
    private String groupId;

    private long expireTime = 5 * 60L; // ttl 시간 // 5분
}
