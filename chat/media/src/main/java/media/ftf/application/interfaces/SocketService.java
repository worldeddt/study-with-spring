package media.ftf.application.interfaces;


import media.ftf.handler.dto.*;
import media.ftf.module.SessionInfo;

import java.security.Principal;


public interface SocketService {

    void handleJoin(Principal user);

    void handleJoin(Principal user, MonitorMessage monitorMessage);

    void handleLeave(SessionInfo sessionInfo);

    void handleOffer(Principal user, OfferMessage offerMessage);

    void handleCandidate(Principal user, CandidateMessage candidateMessage);

    void handleRecord(Principal user, RecordMessage recordMessage);

    void handleChat(Principal user, ChatMessage chatMessage);

    void handleData(Principal user, DataMessage dataMessage);

}
