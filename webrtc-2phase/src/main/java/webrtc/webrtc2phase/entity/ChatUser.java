package webrtc.webrtc2phase.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fermi_chat_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickName;

    private String email;

    private String passwd; // 유저 패스워드

    private String provider;
}
