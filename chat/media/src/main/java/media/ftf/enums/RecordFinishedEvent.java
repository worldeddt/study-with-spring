package media.ftf.enums;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import media.ftf.handler.dto.RecordInfo;

@ToString
@Getter
@Builder
public class RecordFinishedEvent {
    private long id;
    private RecordInfo recordInfo;
}