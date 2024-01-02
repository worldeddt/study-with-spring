package webchat.webrtc4phase;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import webchat.config.KurentoConfig;
import webchat.config.TimerConfig;
import webchat.infra.KmsClientRepository;
import webchat.service.KurentoService;
import webchat.service.RoomService;
import webchat.timer.KurentoRegisterScheduler;




@Component
@AllArgsConstructor
public class WebrtcRunner implements ApplicationRunner {

    private KurentoRegisterScheduler kurentoRegisterScheduler;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        kurentoRegisterScheduler.startTimer();
    }
}
