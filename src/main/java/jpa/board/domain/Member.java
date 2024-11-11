package jpa.board.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jpa.board.dto.MemberDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.ZonedDateTime;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime createdDate;

    @Builder
    public Member(String name, String password, String nickname, RoleType roleType, ZonedDateTime createdDate) {
        this.name = name;
        this.password = password;
        this.nickname = nickname;
        this.roleType = roleType;
        this.createdDate = createdDate;
    }

    public Long updateNickname(String nickname) {
        this.nickname = nickname;
        return no;
    }

    public MemberDto toMemberDto() {
        return new MemberDto(no, name, nickname, createdDate);
    }
}
