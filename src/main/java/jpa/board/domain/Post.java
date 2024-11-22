package jpa.board.domain;

import jakarta.persistence.*;
import jpa.board.dto.PostResponceDto;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "MEMBER_NO")
    private Member member;

    public PostResponceDto toPostDto() {
        return new PostResponceDto(id, title, content, member.toMemberDto());
    }
}
