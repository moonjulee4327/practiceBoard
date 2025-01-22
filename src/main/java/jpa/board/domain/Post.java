package jpa.board.domain;

import jakarta.persistence.*;
import jpa.board.dto.PostDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany
    @OrderBy("id asc")
    private List<Comment> comments = new ArrayList<>();

    public PostDto.Response toPostDto() {
        return new PostDto.Response(id, title, content, member.toMemberDto());
    }

    public Long updatePost(String title, String content) {
        this.title = title;
        this.content = content;
        return id;
    }
}
