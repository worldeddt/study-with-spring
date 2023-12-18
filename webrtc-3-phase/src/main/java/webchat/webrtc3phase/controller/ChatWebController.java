package webchat.webrtc3phase.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatWebController {
    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
