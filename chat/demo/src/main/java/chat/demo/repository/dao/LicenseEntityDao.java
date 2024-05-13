package chat.demo.repository.dao;

public interface LicenseEntityDao {
    long resetCurrentCallWithAllServer();

    // tenantId, serverName 별로 currentCall 체크 (서버 다운 대비용)
    long increaseCurrentCallWithTenantId(int tenantId);

    // tenantId 별로 currentCall 체크
    long increaseCurrentCallWithAllServer(int tenantId);

    // tenantId, serverName 별로 currentCall 체크 (서버 다운 대비용)
    long decreaseCurrentCallWithTenantId(int tenantId, String callServer);

    // tenantId 별로 currentCall 체크
    long decreaseCurrentCallInAllServer(int tenantId);

    long subtractCurrentCallInAllServer(int tenantId, long amount);
}
