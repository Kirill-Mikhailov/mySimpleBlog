package ru.yandex.practicum.mysimpleblog.repository;

import org.springframework.data.domain.Page;
import ru.yandex.practicum.mysimpleblog.model.Post;

import java.util.UUID;

public interface PostRepository {

    Post getPost(UUID id);

    Page<Post> getAll(String search, int pageSize, int pageNumber);

    Post addPost(Post post);

    Post editPost(Post post);

    void deletePost(UUID id);

    void updateLikesCount(UUID postId, boolean like);
}
