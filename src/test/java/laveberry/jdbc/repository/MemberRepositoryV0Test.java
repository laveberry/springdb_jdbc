package laveberry.jdbc.repository;

import laveberry.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();
    @Test
    void crud() throws SQLException {
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
    }

}