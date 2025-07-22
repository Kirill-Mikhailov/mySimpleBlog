package ru.yandex.practicum.mysimpleblog.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.mysimpleblog.dto.CommentDto;
import ru.yandex.practicum.mysimpleblog.dto.mapper.CommentMapper;
import ru.yandex.practicum.mysimpleblog.model.Comment;
import ru.yandex.practicum.mysimpleblog.repository.CommentRepository;
import ru.yandex.practicum.mysimpleblog.service.CommentService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements CommentService {

    CommentRepository commentRepository;
    CommentMapper commentMapper;

    @Override
    public CommentDto addComment(CommentDto commentDto) {
        Comment addedComment = commentRepository.addComment(commentMapper.toComment(commentDto));
        return commentMapper.toCommentDto(addedComment);
    }

    @Override
    public CommentDto editComment(CommentDto commentDto) {
        Comment editedComment = commentRepository.editComment(commentMapper.toComment(commentDto));
        return commentMapper.toCommentDto(editedComment);
    }

    @Override
    public void deleteComment(UUID commentId) {
        commentRepository.deleteComment(commentId);
    }
}
