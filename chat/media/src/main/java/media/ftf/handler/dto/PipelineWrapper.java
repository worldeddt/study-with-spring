package media.ftf.handler.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.kurento.client.MediaPipeline;

@ToString
@Getter
@Builder
public class PipelineWrapper {
    private String url;
    private MediaPipeline mediaPipeline;
}
