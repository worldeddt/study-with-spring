package aop.prototypes.oracle.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@RestController
@RequiredArgsConstructor
public class OracleService implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    private final String queryString2 = """
                        SELECT group_user.id AS guId,
                    group_user.group_id AS group_id,
                    group_user.user_id AS user_id,
                   tbUser.user_type,
                        CASE
                        WHEN tbUser.user_type = 'staff' THEN 1
                        WHEN tbUser.user_type = 'manager' THEN 2
                        ELSE 3
                        END AS user_type_rank,
                   tbUser.device_info,
                   tbUser.name AS user_name,
                   tbUser.login_id AS login_id,
                        TO_CHAR(tbUser.last_login_date, 'YYYYMMDDHH24MISS') AS login_date
                    FROM group_user join
                        tb_user tbUser
                        on group_user.user_id = tbUser.user_id
    """;
    private final String queryString = """ 
            SELECT CASE
                       WHEN EXISTS (SELECT 1
                                    FROM tenant_group
                                    WHERE id = 11 AND ROWNUM = 1)
                           THEN 1
                       ELSE 0
                       END AS SUCCESS
            FROM dual
            """;


    @Override
    public void run(String... args) throws Exception {

        try {
            List<Map<String, Object>> maps = jdbcTemplate.queryForList(queryString);
        } catch (BadSqlGrammarException ex) {
            System.err.println("SQL Syntax Error: " + ex.getMessage());
            System.err.println("Offending SQL: " + ex.getSQLException().getMessage());
        } catch (DataAccessException ex) {
            System.err.println("Data Access Error: " + ex.getMessage());
        }

        AtomicInteger atomicInteger = new AtomicInteger(0);

//        maps.forEach( map -> {
//            System.out.println("result : "+map.get(atomicInteger.getAndAdd(1)));
//        });
    }
}
