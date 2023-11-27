package studymain.studyrepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import studymain.studyrepository.logtrace.LogTrace;
import studymain.studyrepository.logtrace.ThreadLocalLogTrace;

@Configuration
public class LogTraceConfig {

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }
}
