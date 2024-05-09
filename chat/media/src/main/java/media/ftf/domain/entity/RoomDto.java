package media.ftf.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class RoomDto {

    private String id;
    private String userId;
    private Date createDate;
    private Date closeDate;
}
