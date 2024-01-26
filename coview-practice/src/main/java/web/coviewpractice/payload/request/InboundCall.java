package web.coviewpractice.payload.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InboundCall {
    private String groupId;
    private String endDatetime;
}
