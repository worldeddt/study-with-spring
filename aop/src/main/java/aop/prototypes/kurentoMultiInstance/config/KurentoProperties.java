package aop.prototypes.kurentoMultiInstance.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("fermi.kms")
public record KurentoProperties(
        List<String> urls
)
{

}
