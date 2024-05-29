package media.ftf.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import media.ftf.advice.CommonException;
import media.ftf.application.interfaces.SocketService;
import media.ftf.componant.ChatEvent;
import media.ftf.domain.RoomManager;
import media.ftf.domain.entity.RecordEntity;
import media.ftf.domain.entity.RoomEntity;
import media.ftf.enums.*;
import media.ftf.enums.RecordMessage;
import media.ftf.handler.dto.*;
import media.ftf.module.*;
import media.ftf.module.AnswerMessage;
import media.ftf.properties.MediaProperties;
import media.ftf.properties.RecordProperties;
import media.ftf.repository.RecordRepository;
import org.kurento.client.MediaType;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class SocketServiceImpl implements SocketService {

    private final MediaProperties mediaProperties;
    private final RecordProperties recordProperties;

    private final RecordRepository recordRepository;

    private final SessionManager sessionManager;
    private final MonitorManger monitorManger;
    private final BufferManger bufferManger;
    private final RoomManager roomManager;

    private final RoomMessageSender roomMessageSender;
    private final ApplicationEventPublisher applicationEventPublisher;

    private final CommonTtlCache<String> fileNameCache = new CommonTtlCache<>();

    @Override
    public void handleJoin(Principal user) {
        final var sessionInfo = sessionManager.findSessionInfo(user);
        final var userId = sessionInfo.getUserId();
        final var roomId = sessionInfo.getRoomId();
        final var sessionType = sessionInfo.getTicketType();

        if (TicketType.CONFERENCE.equals(sessionType)) {

            final var chatMessage = ChatMessage.builder()
                    .type(EMsgType.NOTICE)
                    .sender(userId)
                    .message("enter")
                    .date(LocalDateTime.now())
                    .build();

            final var chatEvent = ChatEvent.builder()
                    .roomId(roomId)
                    .chatMessage(chatMessage)
                    .build();

            applicationEventPublisher.publishEvent(chatEvent);
            roomMessageSender.sendChat(roomId, chatMessage);
        }
    }

    @Override
    public void handleJoin(Principal user, MonitorMessage monitorMessage) {
        log.debug("{}, {}", user, monitorMessage);

        final var roomId = monitorMessage.getRoomId();

        final var sessionInfo = sessionManager.findSessionInfo(user);
        final var userId = sessionInfo.getUserId();
        final var sessionType = sessionInfo.getTicketType();

        if (!TicketType.MONITOR.equals(sessionType)) {
            throw new CommonException(CommonCode.INVALID_MONITOR_SESSION_TYPE);
        }

        if (!monitorMessage.isLeave()) {
            // join
            monitorManger.joinRoom(userId, roomId, room -> {
                final var otherParticipants = room.findParticipants();
                otherParticipants.forEach(otherParticipant -> {
                    // 나에게 기존참여자 정보를 보냄
                    final var otherSessionInfo = sessionManager.findSessionInfoByUserId(otherParticipant.getUserId());
                    roomMessageSender.sendMemberMessage(user, otherSessionInfo, MemberNotificationType.ENTER);
                });
            });
        } else {
            // leave
            monitorManger.leaveRoom(userId, roomId);
        }

    }

    @Override
    public void handleOffer(Principal user, OfferMessage offerMessage) {
        log.debug("{}, {}", user, offerMessage);

        final var sessionInfo = sessionManager.findSessionInfo(user);
        final var userId = sessionInfo.getUserId();
        final var sessionType = sessionInfo.getTicketType();
        final var roomId = TicketType.CONFERENCE.equals(sessionType) ? sessionInfo.getRoomId() : offerMessage.getRoomId();
        final var epType = offerMessage.getType();
        final var present = offerMessage.getPresent();
        final var offer = offerMessage.getSdp();

        // 모니터링 유저는 incoming 연결만 가능
        if (WebRtcEndpointType.OUTGOING.equals(epType) && TicketType.MONITOR.equals(sessionType)) {
            throw new CommonException(CommonCode.CANNOT_CONNECT_TO_OUTGOING);
        }

        // incoming 은 present 가 세션 정보에 있어야함.
        if (WebRtcEndpointType.INCOMING.equals(epType) && (!StringUtils.hasText(present) || sessionManager.findPrincipalByUserId(present) == null)) {
            throw new CommonException(CommonCode.UNKNOWN_PRESENT_USER);
        }

        roomManager.handleRoomWithThrowNotFoundRoom(roomId, room -> {

            if (TicketType.CONFERENCE.equals(sessionType) && WebRtcEndpointType.OUTGOING.equals(epType)) {
                room.join(userId);
                handleJoin(user);
            }

            final var participant = room.findParticipant(userId);

            final var webRtcEndpoint = participant.getWebRtcPeerDefaultCreate(epType, present);
            webRtcEndpoint.addTag(CommonConstant.TAG_ROOM_ID, roomId);
            webRtcEndpoint.addTag(CommonConstant.TAG_PEER_ID, userId + (present == null ? "" : "." + present));
            webRtcEndpoint.setTurnUrl(mediaProperties.webrtc().turnUrl());

            webRtcEndpoint.addConnectionStateChangedListener(event -> log.info("[webrtc][{}][{}][{}] {} -> {}"
                    , event.getSource().getTag(CommonConstant.TAG_ROOM_ID)
                    , event.getSource().getTag(CommonConstant.TAG_PEER_ID)
                    , event.getType()
                    , event.getOldState()
                    , event.getNewState()
            ));

            webRtcEndpoint.addIceComponentStateChangedListener(event -> log.info("[webrtc][{}][{}][{}] {}"
                    , event.getSource().getTag(CommonConstant.TAG_ROOM_ID)
                    , event.getSource().getTag(CommonConstant.TAG_PEER_ID)
                    , event.getType()
                    , event.getState()
            ));

            webRtcEndpoint.addIceCandidateFoundListener(event -> {
                log.info("[webrtc][{}][{}][{}] {} {} {}"
                        , event.getSource().getTag(CommonConstant.TAG_ROOM_ID)
                        , event.getSource().getTag(CommonConstant.TAG_PEER_ID)
                        , event.getType()
                        , event.getCandidate().getCandidate()
                        , event.getCandidate().getSdpMid()
                        , event.getCandidate().getSdpMLineIndex()
                );
                roomMessageSender.sendCandidate(user, CandidateMessage.builder().roomId(roomId).type(epType).present(present).iceCandidate(event.getCandidate()).build());
            });

            webRtcEndpoint.addIceGatheringDoneListener(event -> log.info("[webrtc][{}][{}][{}]"
                    , event.getSource().getTag(CommonConstant.TAG_ROOM_ID)
                    , event.getSource().getTag(CommonConstant.TAG_PEER_ID)
                    , event.getType()
            ));

            webRtcEndpoint.addMediaSessionStartedListener(event -> log.info("[webrtc][{}][{}][{}]" //1
                    , event.getSource().getTag(CommonConstant.TAG_ROOM_ID)
                    , event.getSource().getTag(CommonConstant.TAG_PEER_ID)
                    , event.getType()
            ));

            webRtcEndpoint.addMediaFlowOutStateChangedListener(event -> log.info("[webrtc][{}][{}][{}] {} {}"
                    , event.getSource().getTag(CommonConstant.TAG_ROOM_ID)
                    , event.getSource().getTag(CommonConstant.TAG_PEER_ID)
                    , event.getType()
                    , event.getMediaType() // [ AUDIO | VIDEO | DATA ]
                    , event.getState()
            ));

            webRtcEndpoint.addMediaFlowInStateChangedListener(event -> log.info("[webrtc][{}][{}][{}] {} {}"
                    , event.getSource().getTag(CommonConstant.TAG_ROOM_ID)
                    , event.getSource().getTag(CommonConstant.TAG_PEER_ID)
                    , event.getType()
                    , event.getMediaType() // [ AUDIO | VIDEO | DATA ]
                    , event.getState()
            ));

            webRtcEndpoint.addMediaStateChangedListener(event -> log.info("[webrtc][{}][{}][{}] {} -> {}"
                    , event.getSource().getTag(CommonConstant.TAG_ROOM_ID)
                    , event.getSource().getTag(CommonConstant.TAG_PEER_ID)
                    , event.getType()
                    , event.getOldState()
                    , event.getNewState()
            ));

            webRtcEndpoint.addMediaTranscodingStateChangedListener(event -> log.info("[webrtc][{}][{}][{}] {}"
                    , event.getSource().getTag(CommonConstant.TAG_ROOM_ID)
                    , event.getSource().getTag(CommonConstant.TAG_PEER_ID)
                    , event.getType()
                    , event.getMediaType() // [ AUDIO | VIDEO | DATA ]
            ));

            webRtcEndpoint.addElementConnectedListener(event -> log.info("[webrtc][{}][{}][{}][{}][{}]"
                    , event.getSource().getTag(CommonConstant.TAG_ROOM_ID)
                    , event.getSource().getTag(CommonConstant.TAG_PEER_ID)
                    , event.getType()
                    , event.getMediaType() // [ AUDIO | VIDEO | DATA ]
                    , event.getSink().getTag(CommonConstant.TAG_PEER_ID)
            ));

            webRtcEndpoint.addErrorListener(event -> log.error("[webrtc][{}][{}][{}] {}"
                    , event.getSource().getTag(CommonConstant.TAG_ROOM_ID)
                    , event.getSource().getTag(CommonConstant.TAG_PEER_ID)
                    , event.getType()
                    , event.getErrorCode()
            ));

            final var sdpAnswer = webRtcEndpoint.processOffer(offer);
            roomMessageSender.sendAnswer(user, AnswerMessage.builder().roomId(roomId).type(epType).present(present).sdp(sdpAnswer).build());

            webRtcEndpoint.gatherCandidates();

            // candidate buffer 비우기
            final var candidateBuffer = bufferManger.getCandidateBuffer(roomId, userId, present);
            final var candidates = candidateBuffer.flush();
            candidates.forEach(iceCandidate -> {
                log.info("[webrtc][{}][{}][addCandidate] candidate: {}, sdpMid: {}, adpMLindeIndex: {}"
                        , webRtcEndpoint.getTag(CommonConstant.TAG_ROOM_ID)
                        , webRtcEndpoint.getTag(CommonConstant.TAG_PEER_ID)
                        , iceCandidate.getCandidate()
                        , iceCandidate.getSdpMid()
                        , iceCandidate.getSdpMLineIndex());
                webRtcEndpoint.addIceCandidate(iceCandidate);
            });

            switch (epType) {
                case OUTGOING -> {
                    // 참여자 알림
                    final var otherParticipants = room.findOtherParticipants(userId);
                    otherParticipants.forEach(otherParticipant -> {
                        final var otherSessionInfo = sessionManager.findSessionInfoByUserId(otherParticipant.getUserId());
                        roomMessageSender.sendMemberMessage(user, otherSessionInfo, MemberNotificationType.ENTER); // 나에게 기존참여자 정보를 보냄
                        roomMessageSender.sendMemberMessage(otherSessionInfo.getPrincipal(), sessionInfo, MemberNotificationType.ENTER);  // 기존 참여자에게 내 참여정보를 보냄
                    });

                }
                case INCOMING -> {
                    // 영상 연결
                    final var presentParticipant = room.findParticipant(present);
                    final var presentOutWebRtcPeer = presentParticipant.getOutWebRtcEndpoint();
                    presentOutWebRtcPeer.connect(webRtcEndpoint, MediaType.AUDIO);
                    presentOutWebRtcPeer.connect(webRtcEndpoint, MediaType.VIDEO);
                }
                default -> throw new CommonException(CommonCode.UNKNOWN_WEBRTC_EP_TYPE);
            }

        });

    }

    @Override
    public void handleCandidate(Principal user, CandidateMessage candidateMessage) {
        log.debug("user: {}, candidateMessage: {}", user, candidateMessage);

        final var sessionInfo = sessionManager.findSessionInfo(user);
        final var sessionType = sessionInfo.getTicketType();
        final var userId = sessionInfo.getUserId();

        final var roomId = TicketType.CONFERENCE.equals(sessionType) ? sessionInfo.getRoomId() : candidateMessage.getRoomId();
        final var epType = candidateMessage.getType();
        final var present = candidateMessage.getPresent();
        final var iceCandidate = candidateMessage.getIceCandidate();

        final var candidateBuffer = bufferManger.getCandidateBuffer(roomId, userId, present);

        if (!candidateBuffer.cache(iceCandidate)) {
            roomManager.handleRoomWithThrowNotFoundRoom(roomId, room -> {

                final var participant = room.findParticipant(userId);
                final var webRtcEndpoint = participant.getWebRtcPeerDefaultCreate(epType, present);

                log.info("[webrtc][{}][{}][addCandidate] candidate: {}, sdpMid: {}, adpMLindeIndex: {}"
                        , webRtcEndpoint.getTag(CommonConstant.TAG_ROOM_ID)
                        , webRtcEndpoint.getTag(CommonConstant.TAG_PEER_ID)
                        , iceCandidate.getCandidate()
                        , iceCandidate.getSdpMid()
                        , iceCandidate.getSdpMLineIndex());

                webRtcEndpoint.addIceCandidate(iceCandidate);
            });
        }
    }

    @Transactional
    @Override
    public void handleRecord(Principal user, RecordMessage recordMessage) {
        final var sessionInfo = sessionManager.findSessionInfo(user);
        final var roomId = sessionInfo.getRoomId();
        final var userId = sessionInfo.getUserId();

        log.debug("레코딩 핸들 SessionInfo: {} ", sessionInfo);

        if (!sessionInfo.isAgent()) {
            throw new CommonException(CommonCode.RECORD_USER_NOT_AGENT);
        }

        roomManager.handleRoomWithThrowNotFoundRoom(roomId, room -> {

            final var recorder = room.getRecorder();

            switch (recordMessage.getType()) {
                case START -> {

                }
                case STOP -> {
                }
            }

        });
    }

    @Override
    public void handleChat(Principal user, ChatMessage chatMessage) {

        // 방 정보 가져와서 방 사람들에서 전체 전송
        final var sessionInfo = sessionManager.findSessionInfo(user);
        final var roomId = sessionInfo.getRoomId();
        final var userId = sessionInfo.getUserId();

        log.debug("[{}][chat] user: {}, chatMessage: {}", roomId, user, chatMessage);

        chatMessage.setSender(userId);

        if (!StringUtils.hasText(chatMessage.getMessage())) {
            throw new CommonException(CommonCode.CHAT_MESSAGE_IS_NULL);
        }

        if (StringUtils.hasText(chatMessage.getReceiver())) {

            final var receiverPrincipal = sessionManager.findPrincipalByUserId(chatMessage.getReceiver());

            if (receiverPrincipal == null)
                throw new CommonException(CommonCode.NOT_FOUND_RECEIVER);

            roomMessageSender.sendChat(receiverPrincipal, chatMessage);
        } else {

            applicationEventPublisher.publishEvent(ChatEvent.builder()
                    .roomId(roomId)
                    .chatMessage(chatMessage)
                    .build());

            roomMessageSender.sendChat(roomId, chatMessage);
        }

    }

    @Override
    public void handleData(Principal user, DataMessage dataMessage) {
        log.debug("user: {}, dataMessage: {}", user, dataMessage);

        // 방 정보 가져와서 방 사람들에서 전체 전송
        final var sessionInfo = sessionManager.findSessionInfo(user);
        final var roomId = sessionInfo.getRoomId();

        dataMessage.setSender(sessionInfo.getUserId());

        if (StringUtils.hasText(dataMessage.getReceiver())) {
            final var receiverPrincipal = sessionManager.findPrincipalByUserId(dataMessage.getReceiver());

            if (receiverPrincipal == null)
                throw new CommonException(CommonCode.UNKNOWN_RECEIVER_USER);

            roomMessageSender.sendData(receiverPrincipal, dataMessage);
        } else {
            roomMessageSender.sendData(roomId, dataMessage);
        }

    }

    @Override
    public void handleLeave(SessionInfo sessionInfo) {
        log.debug("{}", sessionInfo);

        if (sessionInfo == null) return;

        final var userId = sessionInfo.getUserId();
        final var roomId = sessionInfo.getRoomId();
        final var sessionType = sessionInfo.getTicketType();

        bufferManger.release(userId);

        switch (sessionType) {
            case MONITOR -> monitorManger.leaveAllRoom(userId);
            case CONFERENCE -> roomManager.handleRoom(roomId, room -> {
                room.leave(userId);

                final var chatEvent = ChatEvent.builder()
                        .roomId(roomId)
                        .chatMessage(ChatMessage.builder()
                                .type(EMsgType.NOTICE)
                                .sender(userId)
                                .message("leave")
                                .date(LocalDateTime.now())
                                .build())
                        .build();

                applicationEventPublisher.publishEvent(chatEvent);
                roomMessageSender.sendChat(roomId, chatEvent.getChatMessage());

                final var otherParticipants = room.findOtherParticipantsWithMonitoringUser(userId);

                otherParticipants.forEach(participant -> {
                    final var otherParticipant = sessionManager.findPrincipalByUserId(participant.getUserId());
                    roomMessageSender.sendMemberMessage(otherParticipant, sessionInfo, MemberNotificationType.LEAVE);
                });

            });
            default -> throw new CommonException(CommonCode.UNKNOWN_SESSION_TYPE);
        }

    }

}
