package web.coviewpractice.components.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import web.coviewpractice.common.enums.AgentStatus;
import web.coviewpractice.common.enums.FermiErrorCode;
import web.coviewpractice.common.exception.FermiException;
import web.coviewpractice.repository.AgentRedisRepository;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
@Component
public class AgentStatusManager {

    private final RedissonClient redissonClient;
    private final AgentRedisRepository agentRedisRepository;


    public void updateAgentStatus(String userId, AgentStatus agentStatus) {

        final var lock = redissonClient.getLock(String.valueOf(userId));

        try {
            boolean available = lock.tryLock(10L, 1L, TimeUnit.SECONDS);

            if (!available) {
                throw new FermiException(FermiErrorCode.INTERNAL_SERVER_ERROR);
            }

            agentRedisRepository.findById(userId).ifPresent(agentStatusCache -> {

            });

        } catch (InterruptedException ignored) {
            throw new FermiException(FermiErrorCode.INTERNAL_SERVER_ERROR);
        } finally {
            lock.unlock();
        }
    }

    public void toBeOwner(String userId, boolean toBe) {

    }


    public boolean checkStatus(String userId, AgentStatus agentStatus) {

        final var lock  = redissonClient.getLock(String.valueOf(userId));

        try {
            boolean available = lock.tryLock(10L, 1L, TimeUnit.SECONDS);

            if (!available) throw new FermiException(FermiErrorCode.INTERNAL_SERVER_ERROR);

            final var agentStatusCache = agentRedisRepository.findById(userId);
            log.info("agentStatusCache {}", agentStatusCache);
            return agentStatusCache.filter(statusCache -> agentStatus.equals(statusCache.getAgentStatus())).isPresent();

        } catch (InterruptedException e) {
            throw new FermiException(FermiErrorCode.INTERNAL_SERVER_ERROR);
        } finally {
            lock.unlock();
        }
    }

    public boolean isOwner(String userId) {
        final var lock = redissonClient.getLock(String.valueOf(userId));
        try {
            boolean available = lock.tryLock(10L, 1L, TimeUnit.SECONDS);

            if (!available) {
                throw new FermiException(FermiErrorCode.INTERNAL_SERVER_ERROR);
            }
            final var agentStatusCache = agentRedisRepository.findById(userId).get();

            return agentStatusCache.isOwner();
        } catch (InterruptedException ignored) {
            throw new FermiException(FermiErrorCode.INTERNAL_SERVER_ERROR);
        } finally {
            lock.unlock();
        }
    }

    public boolean isLicenseExceeded(String tenantId) {
        final var lock = redissonClient.getLock(String.valueOf(tenantId));
        try {
            boolean available = lock.tryLock(10L, 1L, TimeUnit.SECONDS);

            if (!available) {
                throw new FermiException(FermiErrorCode.INTERNAL_SERVER_ERROR);
            }

            boolean result = false;
            // 테넌트 라이센스 가져오기
            int license = 10;
            // 오너 수로 현재 동시 상담 수 체크
            final var ownerAgentList = agentRedisRepository.findAllByIsOwner(true);

            if (ownerAgentList != null) {
                if (license < ownerAgentList.size()) {
                    log.info("license exceeded!!");
                    result = true;
                }
            }
            return result;
        } catch (InterruptedException e) {
            throw new FermiException(FermiErrorCode.INTERNAL_SERVER_ERROR);
        } finally {
            lock.unlock();
        }
    }
}
