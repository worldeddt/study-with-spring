package media.ftf.utils;

import lombok.*;
import org.springframework.http.server.ServletServerHttpRequest;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IpUtils {

    private static final String CLIENT_IP_KEY = "clientIp";
    private static final String REMOTE_IP_KEY = "remoteIp";


    public static void setIpInfoToAttributes(ServletServerHttpRequest request, Map<String, Object> attributes) {
        attributes.put(CLIENT_IP_KEY, getClientIp(request));
        attributes.put(REMOTE_IP_KEY, getRemoteIp(request));
    }

    public static IpInfo getIpInfo(Map<String, Object> attributes) {
        return IpInfo.builder()
                .clientIp(String.valueOf(attributes.get(CLIENT_IP_KEY)))
                .remoteIp(String.valueOf(attributes.get(REMOTE_IP_KEY)))
                .build();
    }

    public static String getRemoteIp(ServletServerHttpRequest request) {
        final var servletRequest = request.getServletRequest();
        return servletRequest.getRemoteAddr();
    }

    public static String getClientIp(ServletServerHttpRequest request) {
        final var servletRequest = request.getServletRequest();
        final var xForwardedForHeader = servletRequest.getHeader("X-Forwarded-For");

        if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
            // 첫 번째 IP가 실제 클라이언트의 IP 주소입니다.
            return xForwardedForHeader.split(",")[0].trim();
        } else {
            // X-Forwarded-For 헤더가 없을 경우, 기본 리모트 주소를 사용합니다.
            return servletRequest.getRemoteAddr();
        }
    }

    @ToString
    @Getter
    @Builder
    public static class IpInfo {
        private final String clientIp;
        private final String remoteIp;
    }

}
