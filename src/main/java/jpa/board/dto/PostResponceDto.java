package jpa.board.dto;

import jpa.board.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponceDto {
    private Long id;

    private String title;

    private String content;

    private MemberDto memberDto;
}
