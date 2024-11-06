package jpa.board.dto;

import lombok.Data;

@Data
public class CreateMemberDto {
    private String name;

    private String password;

    private String nickname;
}
