package media.ftf.domain;


import org.kurento.client.KurentoClient;

import java.util.concurrent.ConcurrentHashMap;

public class Kurento {



    private final ConcurrentHashMap<String, KurentoClient> clients =
            new ConcurrentHashMap<>();
}
