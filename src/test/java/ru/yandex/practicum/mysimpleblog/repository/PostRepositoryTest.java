package ru.yandex.practicum.mysimpleblog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import ru.yandex.practicum.mysimpleblog.model.Post;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostRepositoryTest extends AbstractPostRepositoryTest {

    @Test
    void getPostShouldReturnPost() {
        Post post = postRepository.getPost(post1.getId());
        assertNotNull(post);
        assertEquals(post1.getId(), post.getId());
    }

    @Test
    void getAllShouldReturnPostsWithTargetTags() {
        String search = post1.getTags().stream().findFirst().get();
        Page<Post> posts = postRepository.getAll(search, 10, 0);

        assertNotNull(posts);
        assertEquals(1, posts.getTotalElements());
        assertEquals(1, posts.getTotalPages());
        assertEquals(post1.getId(), posts.getContent().getFirst().getId());
        assertEquals(post1.getTitle(), posts.getContent().getFirst().getTitle());
        assertEquals(1, posts.getContent().getFirst().getTags().size());
        assertTrue(posts.getContent().getFirst().getTags().contains(search));
    }

    @Test
    void getAllShouldReturnEmptyPage() {
        String search = "ъъъ";
        Page<Post> posts = postRepository.getAll(search, 10, 0);

        assertNotNull(posts);
        assertEquals(0, posts.getTotalElements());
        assertEquals(0, posts.getTotalPages());
        assertTrue(posts.getContent().isEmpty());
    }

    @Test
    void getAllShouldReturnAllPosts() {
        Page<Post> posts = postRepository.getAll(null, 10, 0);

        assertNotNull(posts);
        assertEquals(2, posts.getTotalElements());
        assertEquals(1, posts.getTotalPages());
        assertEquals(2, posts.getContent().size());
    }

    @Test
    void addPostShouldReturnNewPost() {
        UUID postId = UUID.randomUUID();

        Post post = Post.builder()
                .id(postId)
                .title("Название нового комментария")
                .imageUrl(UUID.randomUUID() + ".jpg")
                .text("Текст нового поста")
                .tags(List.of("тег"))
                .build();

        Post newPost = postRepository.addPost(post);

        assertNotNull(newPost.getId());
        assertEquals(post.getId(), newPost.getId());
        assertEquals(post.getTitle(), newPost.getTitle());
        assertEquals(post.getText(), newPost.getText());
        assertEquals(post.getLikesCount(), newPost.getLikesCount());
        assertEquals(post.getImageUrl(), newPost.getImageUrl());
        assertEquals(post.getTags().size(), newPost.getTags().size());
        assertEquals(post.getTags().stream().findFirst().get(), newPost.getTags().stream().findFirst().get());
    }

    @Test
    void editPostShouldReturnUpdatedPost() {
        Post post = Post.builder()
                .id(post1.getId())
                .title("Новое название поста 1")
                .text("Новый текст поста 1")
                .likesCount(post1.getLikesCount())
                .imageUrl(post1.getImageUrl())
                .tags(post1.getTags())
                .comments(post1.getComments())
                .lastChangeTimestamp(post1.getLastChangeTimestamp())
                .build();

        Post editedPost = postRepository.editPost(post);

        assertNotNull(post.getId());
        assertEquals(post.getId(), editedPost.getId());
        assertEquals(post.getTitle(), editedPost.getTitle());
        assertEquals(post.getText(), editedPost.getText());
        assertEquals(post.getLikesCount(), editedPost.getLikesCount());
        assertEquals(post.getImageUrl(), editedPost.getImageUrl());
        assertEquals(post.getTags().size(), editedPost.getTags().size());
        assertEquals(post.getTags().stream().findFirst().get(), editedPost.getTags().stream().findFirst().get());
        assertEquals(post.getComments().size(), editedPost.getComments().size());
        assertEquals(
                post.getComments().stream().findFirst().get(), editedPost.getComments().stream().findFirst().get());
        assertEquals(post.getLastChangeTimestamp().truncatedTo(ChronoUnit.MILLIS),
                editedPost.getLastChangeTimestamp().truncatedTo(ChronoUnit.MILLIS));
    }

    @Test
    void deletePostShouldDeletePost() {
        Page<Post> posts = postRepository.getAll(null, 10, 0);
        assertEquals(2, posts.getTotalElements());

        postRepository.deletePost(post2.getId());

        Page<Post> postsAfterDeleting = postRepository.getAll(null, 10, 0);
        assertEquals(1, postsAfterDeleting.getTotalElements());

        assertThrows(EmptyResultDataAccessException.class, () -> postRepository.getPost(post2.getId()));
    }

    @Test
    void updateLikesCountShouldUpdateLikesCount() {
        assertEquals(postRepository.getPost(post1.getId()).getLikesCount(), post1.getLikesCount());

        postRepository.updateLikesCount(post1.getId(), true);

        assertEquals(postRepository.getPost(post1.getId()).getLikesCount(), post1.getLikesCount() + 1);

        postRepository.updateLikesCount(post1.getId(), false);

        assertEquals(postRepository.getPost(post1.getId()).getLikesCount(), post1.getLikesCount());
    }
}
