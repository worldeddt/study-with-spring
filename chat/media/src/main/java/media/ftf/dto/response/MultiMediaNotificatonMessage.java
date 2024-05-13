package media.ftf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import media.ftf.enums.RoomNotificationType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MultiMediaNotificatonMessage<T> {
    private RoomNotificationType type;
    private T payload;

}
