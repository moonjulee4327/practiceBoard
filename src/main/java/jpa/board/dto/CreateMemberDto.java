package jpa.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateMemberDto {
    private String name;

    private String password;

    private String nickname;
}
