package web.coviewpractice.payload.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HelpCall {
    private String helperId;
    private String endDatetime; // 20231213103259, 기본 5분;
}
