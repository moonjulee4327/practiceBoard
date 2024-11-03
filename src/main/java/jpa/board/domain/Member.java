package jpa.board.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue
    private Long no;

    private String name;

    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Builder
    public Member(String name, String password, String nickname, RoleType roleType) {
        this.name = name;
        this.password = password;
        this.nickname = nickname;
        this.roleType = roleType;
    }
}
