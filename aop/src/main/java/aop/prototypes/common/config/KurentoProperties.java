package aop.prototypes.common.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("kurento")
public record KurentoProperties(
        List<String> urls
)
{

}
