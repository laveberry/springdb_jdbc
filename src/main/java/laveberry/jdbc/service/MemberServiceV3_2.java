package laveberry.jdbc.service;

import laveberry.jdbc.domain.Member;
import laveberry.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;
import java.sql.SQLException;

/*
* 트랜잭션 - 트랜잭션 매니저
* */
@Slf4j
//@RequiredArgsConstructor
public class MemberServiceV3_2 {
//    private final PlatformTransactionManager transactionManager;
    private final TransactionTemplate txTemplate;
    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
        //생성자에서 transactionManager 주입받기
        this.txTemplate = new TransactionTemplate(transactionManager);
        this.memberRepository = memberRepository;
    }

    public void accountTransfer(String fromId, String toId, int money) {
        //트랜잭션 시작
        txTemplate.executeWithoutResult((status) -> {
            //비즈니스 로직
            try {
                bizLogic(fromId, toId, money);
            } catch (SQLException e) {
                //람다안에서는 체크예제 불가. 언체크예제로 바꿔서 던지기
                throw new IllegalStateException(e);
            }
        });
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        // 공통 Connection유지를 위해, con 같이 넘기기!
        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(fromId, money, fromMember, toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
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
