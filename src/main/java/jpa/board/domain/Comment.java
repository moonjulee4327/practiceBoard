package jpa.board.domain;

import jakarta.persistence.*;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
@Entity
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    private String content;

    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "POST_ID")
    private Long postId;
}
