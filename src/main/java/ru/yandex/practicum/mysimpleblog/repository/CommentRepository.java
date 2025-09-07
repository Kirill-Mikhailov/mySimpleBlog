package ru.yandex.practicum.mysimpleblog.repository;

import ru.yandex.practicum.mysimpleblog.model.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentRepository {

    Comment addComment(Comment comment);

    Comment editComment(Comment comment);

    void deleteComment(UUID id);

    List<Comment> getCommentsByPostIds(List<UUID> postIds);
}
