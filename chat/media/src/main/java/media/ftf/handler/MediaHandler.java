package media.ftf.handler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import media.ftf.application.interfaces.SocketService;
import media.ftf.domain.RoomManager;
import media.ftf.handler.dto.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MediaHandler {

    private final SocketService communicationService;
    private final RoomManager roomManager;

    @MessageMapping("/room/monitor")
    public void monitor(SimpMessageHeaderAccessor headerAccessor, @Validated @Payload MonitorMessage monitorMessage) {
        log.info("{}", monitorMessage);
        final var user = headerAccessor.getUser();
        communicationService.handleJoin(user, monitorMessage);
    }

    @MessageMapping("/room/offer")
    public void offer(SimpMessageHeaderAccessor headerAccessor, @Validated @Payload OfferMessage offerMessage) {
        log.info("{}", offerMessage);
        final var user = headerAccessor.getUser();
        communicationService.handleOffer(user, offerMessage);
    }

    @MessageMapping("/room/check/roomId")
    public void check() {
        roomManager.iteratorRoomId();
    }

    @MessageMapping("/room/candidate")
    public void candidate(SimpMessageHeaderAccessor headerAccessor, @Validated @Payload CandidateMessage candidateMessage) {
        log.info("{}", candidateMessage);
        final var user = headerAccessor.getUser();
        communicationService.handleCandidate(user, candidateMessage);
    }

    @MessageMapping("/room/record")
    public void record(SimpMessageHeaderAccessor headerAccessor, @Validated @Payload RecordMessage recordMessage) {
        log.info("{}", recordMessage);
        final var user = headerAccessor.getUser();
        communicationService.handleRecord(user, recordMessage);
    }

    @MessageMapping("/room/chat")
    public void chat(SimpMessageHeaderAccessor headerAccessor, @Validated @Payload ChatMessage chatMessage) {
        final var user = headerAccessor.getUser();
        communicationService.handleChat(user, chatMessage);
    }

    @MessageMapping("/room/bypass")
    public void data(SimpMessageHeaderAccessor headerAccessor, @Validated @Payload DataMessage dataMessage) {
        final var user = headerAccessor.getUser();
        communicationService.handleData(user, dataMessage);
    }
}
