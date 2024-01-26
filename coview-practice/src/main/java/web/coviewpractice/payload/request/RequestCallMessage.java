package web.coviewpractice.payload.request;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import web.coviewpractice.common.enums.CallType;

@ToString
@Setter
@Getter
public class RequestCallMessage {

    private CallType callType;

    // 3자 초대
    private String helperId;
    
    // inbound
    private String groupId;

    public HelpCall getHelpCall() {
        return HelpCall.builder().helperId(helperId).build();
    }

    public InboundCall getInboundCall() {
        return InboundCall.builder().groupId(groupId).build();
    }

}
