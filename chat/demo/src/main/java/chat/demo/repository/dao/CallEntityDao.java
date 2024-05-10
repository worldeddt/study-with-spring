package chat.demo.repository.dao;

import chat.demo.repository.entity.Call;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CallEntityDao {

    Call findCallByCaller(String callerId);
}