package media.ftf.handler.dto;

import com.fermi.multimedia.core.enums.RecordStatus;
import com.fermi.multimedia.core.enums.RecordType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

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
