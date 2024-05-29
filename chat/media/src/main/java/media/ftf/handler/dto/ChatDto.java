package media.ftf.handler.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import media.ftf.enums.EMsgType;

import java.util.Date;


@ToString
@Getter
@Builder
public class ChatDto {
    private EMsgType type;
    private String userId;
    private String message;
    private Date createDate;
}
