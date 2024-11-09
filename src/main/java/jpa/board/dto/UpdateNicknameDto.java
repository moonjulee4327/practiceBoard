package jpa.board.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateNicknameDto {
    private String nickname;

    public UpdateNicknameDto(String nickname) {
        this.nickname = nickname;
    }
}
