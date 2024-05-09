package media.ftf.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.kurento.client.MediaPipeline;

@Getter
@Builder
public class PipelineWrapper {
    private String url;
    private MediaPipeline mediaPipeline;
}

