package webrtc.webrtc2phase.controller.presentations;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateRoom {
   private String roomName;
   private String roomPwd;
   private boolean secretChk;
   private int maxUserCnt;
   private String chatType;

   public static CreateRoom of(
           String roomName,
           String roomPwd,
           boolean secretChk,
           int maxUserCnt,
           String chatType) {
      return CreateRoom.builder()

      .roomName(roomName)
      .roomPwd(roomPwd)
      .secretChk(secretChk)
      .maxUserCnt(maxUserCnt)
      .chatType(chatType)
              .build();
   }
}
