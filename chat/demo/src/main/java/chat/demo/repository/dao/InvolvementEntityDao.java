package chat.demo.repository.dao;


import java.util.List;

public interface InvolvementEntityDao {

    Long countHandoverTodayByUserId(String userId);

    String findCurrentOwnerByCallId(String callId);

}
