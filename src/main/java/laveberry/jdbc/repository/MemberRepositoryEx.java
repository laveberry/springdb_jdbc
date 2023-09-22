package laveberry.jdbc.repository;

import laveberry.jdbc.domain.Member;

import java.sql.SQLException;

public interface MemberRepositoryEx {
    //체크 예외가 구현체에 있을시 인터페이스도 오류처리 필요(비효율)
    Member save(Member member) throws SQLException;
    Member findById(String memberId) throws SQLException;
    void update(String memberId, int money) throws SQLException;
    void delete(String memberId) throws SQLException;
}
