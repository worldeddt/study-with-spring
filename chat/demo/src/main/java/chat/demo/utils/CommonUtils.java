package chat.demo.utils;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;


import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class CommonUtils {

    public static HttpClient httpClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        // 모든 인증서를 신뢰하도록 설정한다
        final var sslContext = new SSLContextBuilder().loadTrustMaterial(null, (X509Certificate[] chain, String authType) -> true).build();

        // Https 인증 요청시 호스트네임 유효성 검사를 진행하지 않게 한다.
        final var sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        final var socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory).build();
        final var connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);


        final var httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(connMgr);
        return httpClientBuilder.build();
    }
}
