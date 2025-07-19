package ru.yandex.practicum.mysimpleblog.dto.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.mysimpleblog.dto.PostDto;
import ru.yandex.practicum.mysimpleblog.model.Post;

@Mapper
public interface PostMapper {

    Post toPost(PostDto postDto);

    PostDto toPostDto(Post post);
}
