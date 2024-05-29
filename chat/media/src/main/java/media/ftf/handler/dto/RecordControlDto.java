package media.ftf.handler.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import media.ftf.enums.RecordControlType;

import java.util.List;

@ToString
@Getter
@Builder
public class RecordControlDto {
    private RecordControlType state;
    private List<RecordCheckDto> data;
}
