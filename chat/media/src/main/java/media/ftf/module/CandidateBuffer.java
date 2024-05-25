package media.ftf.module;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.IceCandidate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
public class CandidateBuffer {

    private final List<IceCandidate> buffer = new ArrayList<>();
    private boolean flush = false;

    public synchronized boolean cache(IceCandidate iceCandidate) {
        if (!flush) {
            buffer.add(iceCandidate);
            return true;
        }
        return false;
    }

    public synchronized List<IceCandidate> flush() {
        flush = true;
        final var copyList = List.copyOf(buffer);
        buffer.clear();
        return copyList;
    }
}
