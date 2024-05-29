package media.ftf.module;


import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import media.ftf.properties.RecordProperties;
import org.kurento.client.MediaPipeline;

import java.util.Map;
import java.util.function.Supplier;

@Slf4j
public class Recorder {

    private final String KURENTO_RECORD_PREFIX = "file://";

    private boolean isRecord;

    @Builder
    public Recorder(
            RecordProperties recordProperties,
            Supplier<MediaPipeline> getMediaPipeline,
            Supplier<Map<String, Participant>> getParticipants
    ) {

        final var mediaPipeline = getMediaPipeline.get();
    }

}