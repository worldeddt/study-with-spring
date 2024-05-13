package chat.demo.repository.dao;

import chat.demo.controller.dto.request.CallStatisticsRequest;
import chat.demo.controller.dto.response.HistoryResponse;
import chat.demo.repository.entity.Call;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CallEntityDao {

    Call selectByCallerId(String userId);

    Call selectByOwnerId(String agentId);
}
