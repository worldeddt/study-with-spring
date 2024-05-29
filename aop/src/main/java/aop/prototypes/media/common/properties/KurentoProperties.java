package aop.prototypes.media.common.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("fermi.kms")
public record KurentoProperties(
        List<String> urls
)
{

}
