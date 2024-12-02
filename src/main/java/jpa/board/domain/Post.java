package jpa.board.domain;

import jakarta.persistence.*;
import jpa.board.dto.PostResponseDto;
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

    @Column(length = 60)
    private String title;

    @Column(length = 6000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "MEMBER_NO")
    private Member member;

    public PostResponseDto toPostDto() {
        return new PostResponseDto(id, title, content, member.toMemberDto());
    }

    public Long updatePost(String title, String content) {
        this.title = title;
        this.content = content;
        return id;
    }
}
