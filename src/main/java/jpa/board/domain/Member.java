package jpa.board.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public Long updateNickname(String nickname) {
        this.nickname = nickname;
        return no;
    }
}
