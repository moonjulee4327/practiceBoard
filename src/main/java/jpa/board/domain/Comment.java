package jpa.board.domain;

import jakarta.persistence.*;
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

    @Column(name = "POST_ID", nullable = false)
    private Long postId;
}
