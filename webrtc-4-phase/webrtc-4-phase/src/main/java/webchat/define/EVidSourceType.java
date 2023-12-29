package webchat.define;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;


@Getter
@AllArgsConstructor
public enum EVidSourceType {

    CAMERA(0x01000000, "Both"),
    MIC(0x02000000, "Audio Only"),
    SCREEN_SHARE(0x03000000, "Screen Share"),
    CANVAS_DRAWING(0x04000000, "Canvas Drawing"),
    VIDEO_TOSS(0x05000000, "Video Toss"),
    SIP(0x06000000, "SIP"),
    RECORD_ONLY(0x07000000, "Record Only");

    private int code;
    private String msg;

    public static final int MASK = 0xff000000;
    public static final int MAGIC = 0x00ffffff;

    public static EVidSourceType findByCode(int code)
    {
        return Arrays.stream(EVidSourceType.values())
                .filter(rcvCode->rcvCode.equals(code))
                .findAny()
                .orElse(null);
    }

    public boolean equals(int code){
        return this.code == code;
    }

}
