package chat.demo.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("chat")
public record ChatProperties(
        HttpClientProperties httpClient,

        ChatServerProperties chatServer

) {

    public static record HttpClientProperties(
            ServerProperties server
    ) {

        public static record ServerProperties(
                MultimediaProperties multimedia
        ) {

            public static record MultimediaProperties (
                String entryPoint
            ) {

            }
        }
    }

    public record ChatServerProperties(
            String serverName,
            List<Long> maxCallCount,
            List<Integer> tenantId
    ) {

    }
}
