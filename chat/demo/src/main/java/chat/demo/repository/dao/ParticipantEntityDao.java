package chat.demo.repository.dao;


import chat.demo.repository.entity.Participant;

public interface ParticipantEntityDao {
    Participant findByUserIdWithCallId(String callId, String userId);
}
