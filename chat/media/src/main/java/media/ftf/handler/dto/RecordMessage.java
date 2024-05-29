package media.ftf.handler.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import media.ftf.enums.RecordControlType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordMessage {
    private RecordControlType type;
}
