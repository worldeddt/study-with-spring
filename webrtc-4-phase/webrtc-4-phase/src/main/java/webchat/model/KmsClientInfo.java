package webchat.model;


import lombok.Data;
import org.kurento.client.KurentoClient;
import webchat.define.EMsStatus;

import java.util.List;

@Data
public class KmsClientInfo implements Comparable<KmsClientInfo> {
    private String kmsUrl = null;
    private KurentoClient kurento = null;
    private EMsStatus status = EMsStatus.UR;
    private int pipeCnt;
    private int outEpCnt;
    private int inEpCnt;
    private List<String> roomIds;

    @Override
    public int compareTo(KmsClientInfo kmsClientInfo) {
        return (int)(this.pipeCnt - kmsClientInfo.getPipeCnt());
    }

    public void reset() {
        this.status = EMsStatus.UR;
        this.pipeCnt = 0;
        this.outEpCnt = 0;
        this.inEpCnt = 0;
        this.roomIds.clear();
    }
}
