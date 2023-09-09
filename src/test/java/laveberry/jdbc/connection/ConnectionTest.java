package laveberry.jdbc.connection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static laveberry.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {

    //DriverManager 사용 커넥션 획득 (파라미터 계속 전달해야함)
    @Test
    void driverManager() throws SQLException {
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("connection={}, class={}", con1, con1.getClass());
        log.info("connection={}, class={}", con2, con2.getClass());
    }

    //DataSource 사용 커넥션 획득 (재사용 가능)
    //설정과 사용 분리
    @Test
    void dataSourceDriverManager() throws SQLException {
        //설정
        //DriverManagerDataSource - 항상 새로운 커넥션을 획득
        DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        //사용
        useDataSource(dataSource);
    }

    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();
        log.info("connection={}, class={}", con1, con1.getClass());
        log.info("connection={}, class={}", con2, con2.getClass());
    }
}
