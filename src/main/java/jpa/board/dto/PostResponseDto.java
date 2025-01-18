package jpa.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;

    private String title;

    private String content;

    private MemberDto.Response memberDto;
}
