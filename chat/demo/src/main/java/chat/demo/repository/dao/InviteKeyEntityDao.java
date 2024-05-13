package chat.demo.repository.dao;

import chat.demo.controller.dto.response.InviteKeyPayload;
import chat.demo.repository.entity.InviteKey;

import java.util.List;

public interface InviteKeyEntityDao {

    InviteKeyResponse findInfoByKey(String inviteKey);

    Long expireAllByServer(String serverName);

    Long expireAllByUserId(String userId);

    Long expireById(String inviteKey);

    Long expireAll();

    Long useById(String inviteKey);

    List<InviteKey> findLiveKeyByUserId(String userId);
}
