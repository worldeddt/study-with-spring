package chat.demo.repository.dao;


import chat.demo.repository.entity.Option;

import java.util.List;

public interface OptionEntityDao {
    List<Option> findByCallIdAndUserId(String callId, String userId);
}
