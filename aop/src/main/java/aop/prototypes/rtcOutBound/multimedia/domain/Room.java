package aop.prototypes.rtcOutBound.multimedia.domain;


import lombok.Builder;
import lombok.Getter;
import org.kurento.client.MediaPipeline;

@Getter
public class Room {
    private String roomId;
    private MediaPipeline mediaPipeline;


    @Builder
    public Room (
            String roomId,
            MediaPipeline mediaPipeline
    ) {
        this.roomId = roomId;
        this.mediaPipeline = mediaPipeline;
    }
}
