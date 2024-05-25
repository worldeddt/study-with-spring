package media.ftf.handler.dto;

import com.fermi.multimedia.core.enums.RecordControlType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Builder
public class RecordControlDto {
    private RecordControlType state;
    private List<RecordCheckDto> data;
}
