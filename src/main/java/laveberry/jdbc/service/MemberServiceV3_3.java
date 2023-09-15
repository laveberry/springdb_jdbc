package laveberry.jdbc.service;

import laveberry.jdbc.domain.Member;
import laveberry.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/*
* 트랜잭션 - @Transactional AOP
* */
@Slf4j
public class MemberServiceV3_3 {

    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_3(MemberRepositoryV3 memberRepository) {
        this.memberRepository = memberRepository;
    }

    //비즈니스 로직만 집중
    @Transactional
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        bizLogic(fromId, toId, money);
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        // 공통 Connection유지를 위해, con 같이 넘기기!
        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(fromId, money, fromMember, toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(String fromId, int money, Member fromMember, Member toMember) throws SQLException {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
