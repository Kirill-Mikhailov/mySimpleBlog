package ru.yandex.practicum.mysimpleblog.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.mysimpleblog.dto.PostDto;

import java.util.UUID;

public interface PostService {

    PostDto getPost(UUID id);

    Page<PostDto> getAll(String search, int pageSize, int pageNumber);

    PostDto addPost(PostDto postDto, MultipartFile image);

    PostDto editPost(PostDto postDto, MultipartFile image);

    void deletePost(UUID id);

    void updateLikesCount(UUID postId, boolean like);
}
