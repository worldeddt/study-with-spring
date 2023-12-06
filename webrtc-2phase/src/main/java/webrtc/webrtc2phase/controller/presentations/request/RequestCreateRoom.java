package webrtc.webrtc2phase.controller.presentations.request;


import lombok.Data;

@Data
public class RequestCreateRoom {
    private String roomName;
    private String roomPwd;
    private String secretChk;
    private String maxUserCnt;
    private String chatType;
}
