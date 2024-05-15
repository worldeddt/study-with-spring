package chat.demo.dao;

import chat.demo.repository.dao.CallEntityDao;
import chat.demo.repository.entity.Call;

public class CallEntityDaoImpl implements CallEntityDao {
    @Override
    public Call selectByCallerId(String userId) {
        return null;
    }

    @Override
    public Call selectByOwnerId(String agentId) {
        return null;
    }
}
