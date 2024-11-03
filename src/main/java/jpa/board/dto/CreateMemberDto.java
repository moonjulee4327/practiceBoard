package jpa.board.dto;

import jpa.board.domain.RoleType;
import lombok.Data;

@Data
public class CreateMemberDto {
    private String name;

    private String password;

    private String nickname;

    private RoleType roleType;
}
