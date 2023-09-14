package laveberry.jdbc.service;

import laveberry.jdbc.domain.Member;
import laveberry.jdbc.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MemberServiceV1 {
    private final MemberRepositoryV1 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

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
