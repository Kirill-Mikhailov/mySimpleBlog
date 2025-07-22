package ru.yandex.practicum.mysimpleblog.service;

import ru.yandex.practicum.mysimpleblog.dto.CommentDto;

import java.util.UUID;

public interface CommentService {

    CommentDto addComment(CommentDto commentDto);

    CommentDto editComment(CommentDto commentDto);

    void deleteComment(UUID commentId);
}
