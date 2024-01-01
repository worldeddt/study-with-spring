package webchat.event;

import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;


public class FermiConnectMessageParser extends SimpMessageHeaderAccessor {

    public static final String LOGIN_ID = "login";
    public static final String USER_ID = "userId";
    public static final String USER_Type = "userType";
    public static final String SUBS_ID = "subsId";
    public static final String SID = "sid";

    protected FermiConnectMessageParser(Message<?> message) {
        super(message);
    }

    public static FermiConnectMessageParser wrap(Message<?> message) {
        FermiConnectMessageParser instance = new FermiConnectMessageParser(message);
        FermiConnectMessageParser result = null;
        SimpMessageType type = instance.getMessageType();

        if(type == SimpMessageType.CONNECT_ACK) {
            Message<?> m = (Message<?>) instance.getHeader(CONNECT_MESSAGE_HEADER);
            if(m != null) {
                result = FermiConnectMessageParser.wrap(m);
            }
        }

        if(type == SimpMessageType.CONNECT) {
            result = instance;
        }

        return result;
    }

    public String getLoginId() {
        List<String> list = getNativeHeader(LOGIN_ID);
        String result = null;
        if(list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }

    public String getUserId() {
        List<String> list = getNativeHeader(USER_ID);
        String result = "NA";
        if(list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }

    public String getUserType() {
        List<String> list = getNativeHeader(USER_Type);
        String result = null;
        if(list.size() > 0) {
            result = list.get(0).toUpperCase();
        }
        return result;
    }

    public String getSubsId() {
        List<String> list = getNativeHeader(SUBS_ID);
        String result = null;
        if(list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }

    // 220208
    public String getSid() {
        List<String> list = getNativeHeader(SID);
        String result = null;
        if(list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }
}
