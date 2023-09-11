package laveberry.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import laveberry.jdbc.connection.ConnectionConst;
import laveberry.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.CommonDataSource;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import static laveberry.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV1Test {

    // 직접 만들기
    MemberRepositoryV1 repository;

    @BeforeEach
    void beforeEach() {
        //기본 DriverManager - 항상 새로운 커넥션 획득
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        //커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        //나머지 기본값 세팅됨

        repository = new MemberRepositoryV1(dataSource);
    }


    @Test
    void crud() throws SQLException, InterruptedException {
        //save
        Member member = new Member("memberV100", 10000);
        repository.save(member);

        //findById
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember={}", findMember);
        log.info("member == findMember {}", member == findMember); //false
        log.info("member equals findMember {}", member.equals(findMember)); //true
        assertThat(findMember).isEqualTo(member); //true

        //update : money : 10000 -> 20000
        repository.update(member.getMemberId(), 20000);
        Member updateMember = repository.findById(member.getMemberId());
//        assertThat(updateMember).isEqualTo(member); //오류
        assertThat(updateMember.getMoney()).isEqualTo(20000);

//        if (true) {
//            throw new IllegalStateException("....");
//        }

        //delete
        repository.delete(member.getMemberId());

        //삭제 검증 -> NoSuchElementException 뜨는지
        assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

        Thread.sleep(1000);
    }

}