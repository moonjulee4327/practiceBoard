package jpa.board.service;

import jpa.board.repository.CommentRepository;
import jpa.board.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private SecurityContextService securityContextService;

    @InjectMocks
    private CommentService commentService;

    @Test
    void addCommentToPost() {
    }

    @Test
    void findCommentToPost() {
    }

    @Test
    void updateCommentToPost() {
    }

    @Test
    void deleteCommentById() {
    }
}