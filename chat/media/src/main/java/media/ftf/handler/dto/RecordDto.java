package media.ftf.handler.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import media.ftf.enums.RecordStatus;
import media.ftf.enums.RecordType;

import java.util.Date;

@ToString
@Getter
@Builder
public class RecordDto {
    private long id;
    private String fileName;
    private String orgSuffix;
    private String filePath;
    private long fileSize;
    private Date createDate;
    private RecordType type;
    private RecordStatus status;
}
