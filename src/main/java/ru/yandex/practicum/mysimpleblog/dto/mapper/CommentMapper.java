package ru.yandex.practicum.mysimpleblog.dto.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.mysimpleblog.dto.CommentDto;
import ru.yandex.practicum.mysimpleblog.model.Comment;

@Mapper
public interface CommentMapper {

    Comment toComment(CommentDto commentDto);

    CommentDto toCommentDto(Comment comment);
}
