package webrtc.webrtc2phase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserDto {
    private Long id; // DB 저장되는 id : PK
    private String nickName; // 소셜에서 제공받은 유저명 => 유저 닉네임
    private String passwd; // 유저 패스워드
    private String email; // 소셜에서 제공받은 이메일
    private String provider; // 소셜 제공자 -> ex) 네이버, 카카오 ----
}
