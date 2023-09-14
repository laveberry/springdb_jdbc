package laveberry.jdbc.service;

import laveberry.jdbc.domain.Member;
import laveberry.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/*
* 트랜젝션 - 파라미터 연동, 풀을 고려한 종료
* */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {
    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            //트랜젝션 시작
            con.setAutoCommit(false);
            //비즈니스 로직 시작
            bizLogic(con, fromId, toId, money);
            con.commit(); //성공시 커밋
        } catch (Exception e) {
            con.rollback(); // 실패시 롤백
            throw new IllegalStateException(e);
        } finally {
            release(con);
        }
    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        // 공통 Connection유지를 위해, con 같이 넘기기!
        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(fromId, money, fromMember, toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);
    }

    private static void release(Connection con) {
        if(con != null) {
            try {
                con.setAutoCommit(true); //커넥션 풀 고려해 원복
                con.close();
            } catch (Exception e) {
                log.info("error message={}", "여기도 작성가능", e);
            }
        }
    }

    private void validation(String fromId, int money, Member fromMember, Member toMember) throws SQLException {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
