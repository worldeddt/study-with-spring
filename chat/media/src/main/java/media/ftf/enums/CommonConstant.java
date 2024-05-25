package media.ftf.enums;

import org.springframework.http.HttpHeaders;

public class CommonConstant {

    public static final String FERMI_AUTH_TOKEN_HEADER = "X-Fermi-Auth-Token";
    public static final String BEARER_TOKEN_HEADER = HttpHeaders.AUTHORIZATION;

    public static final String TAG_ROOM_ID = "roomId";
    public static final String TAG_PEER_ID = "peerId";
    public static final String ENC_SUFFIX = ".enc";
}