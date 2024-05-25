package media.ftf.handler.dto;


import com.fermi.multimedia.core.enums.EMsgType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

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
