//package aop.prototypes.oracle.config;
//
//
//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.JSchException;
//import com.jcraft.jsch.Session;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import javax.sql.DataSource;
//
//@Configuration
//@RequiredArgsConstructor
//public class SshPortForswarding {
//
//    private final OracleProperties oracleProperties;
//
//    @Order(0)
//    @Bean
//    public Session setupPortForwarding() throws JSchException {
//        String sshHost = "192.168.0.205";
//        int sshPort = 22;
//        String sshUser = oracleProperties.getUsername();
//        String sshPassword = oracleProperties.getPassword();
//        int localPort = 1521;
//        String remoteHost = "192.168.0.201";
//        int remotePort = 1521;
//
//        JSch jsch = new JSch();
//        Session session = jsch.getSession(sshUser, sshHost, sshPort);
//        session.setPassword(sshPassword);
//
//        // Disable strict host key checking
//        session.setConfig("StrictHostKeyChecking", "no");
//
//        session.connect();
//        session.setPortForwardingL(localPort, remoteHost, remotePort);
//
//        return session;
//    }
//
//
//    @Order(1)
//    @Bean
//    public DataSource dataSource() {
//        org.springframework.boot.jdbc.DataSourceBuilder<?> dataSourceBuilder =
//                org.springframework.boot.jdbc.DataSourceBuilder.create();
//        dataSourceBuilder.url(oracleProperties.getDbUrl());
//        dataSourceBuilder.username(oracleProperties.getDbUsername());
//        dataSourceBuilder.password(oracleProperties.getDbPassword());
//        return dataSourceBuilder.build();
//    }
//
//    @Order(2)
//    @Bean
//    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//}
