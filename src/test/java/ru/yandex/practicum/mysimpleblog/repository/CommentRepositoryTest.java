package ru.yandex.practicum.mysimpleblog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.mysimpleblog.model.Comment;
import ru.yandex.practicum.mysimpleblog.model.Post;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommentRepositoryTest extends AbstractPostRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Test
    void addCommentShouldAddNewComment() {
        UUID commentId = UUID.randomUUID();

        Comment comment = Comment.builder()
                .id(commentId)
                .postId(post1.getId())
                .text("Новый комментарий")
                .build();

        Comment newComment = commentRepository.addComment(comment);

        assertNotNull(newComment.getId());
        assertEquals(comment.getId(), newComment.getId());
        assertEquals(comment.getPostId(), newComment.getPostId());
        assertEquals(comment.getText(), newComment.getText());
    }
    @Test
    void editCommentShouldReturnEditedComment() {
        Comment comment = Comment.builder()
                .id(post1.getComments().getFirst().getId())
                .postId(post1.getId())
                .text("Обновленный комментарий")
                .build();

        Comment editedComment = commentRepository.editComment(comment);

        assertNotNull(editedComment.getId());
        assertEquals(comment.getId(), editedComment.getId());
        assertEquals(comment.getPostId(), editedComment.getPostId());
        assertEquals(comment.getText(), editedComment.getText());
    }

    @Test
    void deleteCommentShouldDeleteComment() {
        Post post = postRepository.getPost(post1.getId());

        assertEquals(1, post.getComments().size());

        commentRepository.deleteComment(post.getComments().getFirst().getId());

        Post postAfterDeletingComment = postRepository.getPost(post1.getId());

        assertEquals(0, postAfterDeletingComment.getComments().size());
    }

    @Test
    void getCommentsByPostIdsShouldReturnCommentsForPost() {
        List<Comment> comments = commentRepository.getCommentsByPostIds(List.of(post1.getId()));

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertTrue(comments.contains(post1.getComments().getFirst()));
    }
}
