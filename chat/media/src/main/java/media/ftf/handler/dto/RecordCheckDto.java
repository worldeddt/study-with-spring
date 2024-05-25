package media.ftf.handler.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class RecordCheckDto {
    // private long id;
    // private String roomId;
    private String userId;
    private int recordStatus;
    private String reason;

}

