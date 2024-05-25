package media.ftf.handler.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import media.ftf.enums.RoomNotificationType;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MultiMediaNotificationMessage<T> {
    private RoomNotificationType type;
    private T payload;

    public static class RecordCheckResponseMessage extends MultiMediaNotificationMessage<RecordCheckDto> {
    }

    public static class RecordResponseMessage extends MultiMediaNotificationMessage<RecordControlDto> {
    }

    public static class MemberResponseMessage extends MultiMediaNotificationMessage<MemberDto> {
    }

    public static class EndRoomResponseMessage extends MultiMediaNotificationMessage<EndRoomDto> {
    }
}
