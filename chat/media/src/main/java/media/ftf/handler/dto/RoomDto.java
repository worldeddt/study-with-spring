package media.ftf.handler.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
@Builder
public class RoomDto {

    private String id;
    private String userId;
    private Date createDate;
    private Date closeDate;
}
