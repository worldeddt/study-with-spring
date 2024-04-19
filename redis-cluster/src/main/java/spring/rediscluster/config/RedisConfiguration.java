package spring.rediscluster.config;


import io.lettuce.core.ReadFrom;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;


@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class RedisConfiguration {

    private final RedisProperties redisProperties;

//    @Bean
//    public RedisConnectionFactory redisConnectionFactory(){
//        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
//                .readFrom(ReadFrom.REPLICA_PREFERRED) // 복제본 노드에서 읽지 만 사용할 수없는 경우 마스터에서 읽습니다.
//                .build();
//        // 모든 클러스터(master, slave) 정보를 적는다. (해당 서버중 접속되는 서버에서 cluster nodes 명령어를 통해 모든 클러스터 정보를 읽어오기에 다운 됐을 경우를 대비하여 모든 노드 정보를 적어두는편이 좋다.)
//        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration()
//                .clusterNode("10.1.0.4", 6300)
//                .clusterNode("10.1.0.5", 6301)
//                .clusterNode("10.1.0.6", 6302)
//                .clusterNode("10.1.0.4", 6400)
//                .clusterNode("10.1.0.5", 6401)
//                .clusterNode("10.1.0.6", 6402);
//        redisClusterConfiguration.setPassword("4444444");
//
//        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisClusterConfiguration, clientConfiguration);
//        return lettuceConnectionFactory;
//    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        var clusterNodes = redisProperties.getCluster().getNodes();


        if (clusterNodes.isEmpty()) {
            /*
            - redis standAlone (1개)
             */
            var redisStandaloneConfiguration = new RedisStandaloneConfiguration();
            redisStandaloneConfiguration.setHostName(redisProperties.getHost());
            redisStandaloneConfiguration.setPort(redisProperties.getPort());
            redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
            return new LettuceConnectionFactory(redisStandaloneConfiguration);
        } else {
            log.info("cluster node 1 : {}", clusterNodes.get(0));
            /*
            - redis cluster (다중)
            1. 클러스터 토폴로지 설정
            2. 클라이언트 설정
            3. host 설정
            4. Lettuce 클라이언트 설정 동기화
             */
            // 클러스터 토폴로지 정보를 클라이언트에 동기화 하는 설정
            var clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                    .enableAllAdaptiveRefreshTriggers()
                    .enablePeriodicRefresh(Duration.ofSeconds(30L))
                    .build();
            var clientOptions = ClusterClientOptions.builder()
                    .topologyRefreshOptions(clusterTopologyRefreshOptions)
                    .build();
            // docker 로 redis 를 연결하기 위한 설정 -> docker.host 가 localhost 와 다른 경우
//        var resolver = MappingSocketAddressResolver.create(DnsResolvers.UNRESOLVED, hostAndPort
//                -> HostAndPort.of(redisProperties.getHost(), redisProperties.getPort()));
//        var clientResources = ClientResources.builder()
//                .socketAddressResolver(resolver)
//                .build();
            // Lettuce 클라이언트 설정
            var clientConfiguration = LettuceClientConfiguration.builder()
                    .commandTimeout(Duration.of(10, ChronoUnit.SECONDS))
                    .clientOptions(clientOptions)
//                .clientResources(clientResources)
                    .readFrom(ReadFrom.REPLICA_PREFERRED)
                    .build();

            var redisClusterConfiguration = new RedisClusterConfiguration(clusterNodes);
            redisClusterConfiguration.setMaxRedirects(redisProperties.getCluster().getMaxRedirects());
            redisClusterConfiguration.setPassword(redisProperties.getPassword());
            return new LettuceConnectionFactory(redisClusterConfiguration, clientConfiguration);
        }
    }



    @Bean
    public RedisTemplate<String, String> redisTemplate(
            RedisConnectionFactory redisConnectionFactory
    ) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
