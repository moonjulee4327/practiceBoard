package jpa.board.domain;

import jakarta.persistence.*;
import jpa.board.dto.CommentDto;
import lombok.*;

import java.time.ZonedDateTime;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 6000)
    private String comment;

    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public CommentDto.Response toCommentDto() {
        return new CommentDto.Response(id, member.getNickname(), comment, createdDate);
    }
}
