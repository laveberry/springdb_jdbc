package laveberry.jdbc.domain;

import lombok.Data;
import lombok.Getter;

@Data //equals hashCode toString 다 만들어줌
public class Member {
    private String memberId;
    private int money;

    public Member() {
    }

    public Member(String memberId, int money) {
        this.memberId = memberId;
        this.money = money;
    }
}
