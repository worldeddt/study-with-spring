package media.ftf.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.concurrent.TimeUnit;


@ConfigurationProperties(prefix = "fermi.record")
public record RecordProperties(
        TypeProperties type,
        ConfigProperties config
) {

    public record TypeProperties(
            ModeProperties each,
            ModeProperties serverComposite,
            ModeProperties clientComposite
    ) {
        public record ModeProperties(
                boolean use,
                String mode
        ) {
        }
    }

    public record ConfigProperties(
            String hostPath,
            String path,
            boolean fileKeep,
            CustomProperties custom,
            TtlProperties ttl
    ) {
        public record TtlProperties(
                TimeUnit timeUnit,
                long checkTtl) {
        }

        public record CustomProperties(
                List<String> option
        ) {

        }
    }

}