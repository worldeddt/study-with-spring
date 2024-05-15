package chat.demo.repository;


import chat.demo.enums.UserStatus;
import chat.demo.repository.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;

public interface UserEntityRepository extends JpaRepository<User, String> {

    // LockMode 로 데드락 걸릴 수 있으므로 함부로 사용하지 말 것
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    User findByUserId(String userId);

    void deleteAllByServer(String serverName);

    User findByCallId(String callId);

    List<User> findAllByUserStatus(UserStatus userStatus);
}
