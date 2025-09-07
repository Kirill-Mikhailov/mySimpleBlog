package ru.yandex.practicum.mysimpleblog.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.mysimpleblog.dto.PostDto;
import ru.yandex.practicum.mysimpleblog.dto.mapper.PostMapper;
import ru.yandex.practicum.mysimpleblog.model.Post;
import ru.yandex.practicum.mysimpleblog.repository.PostRepository;
import ru.yandex.practicum.mysimpleblog.service.ImageService;
import ru.yandex.practicum.mysimpleblog.service.PostService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    PostRepository postRepository;
    ImageService imageService;
    PostMapper postMapper;

    @Override
    public PostDto getPost(UUID id) {
        return postMapper.toPostDto(postRepository.getPost(id));
    }

    @Override
    public Page<PostDto> getAll(String search, int pageSize, int pageNumber) {
        Page<Post> posts = postRepository.getAll(search, pageSize, pageNumber);
        return posts.map(postMapper::toPostDto);
    }

    @Override
    public PostDto addPost(PostDto postDto, MultipartFile image) {
        String imageUrl = imageService.addImage(image);
        postDto.setImageUrl(imageUrl);
        Post addedPost = postRepository.addPost(postMapper.toPost(postDto));
        return postMapper.toPostDto(addedPost);
    }

    @Override
    public PostDto editPost(PostDto postDto, MultipartFile image) {
        Post oldPost = postRepository.getPost(postDto.getId());

        if (image == null || image.isEmpty()) {
            postDto.setImageUrl(oldPost.getImageUrl());
        } else {
            imageService.deleteImage(oldPost.getImageUrl());
            String imageUrl = imageService.addImage(image);
            postDto.setImageUrl(imageUrl);
        }

        Post editedPost = postRepository.editPost(postMapper.toPost(postDto));
        return postMapper.toPostDto(editedPost);
    }

    @Override
    public void deletePost(UUID id) {
        Post oldPost = postRepository.getPost(id);
        imageService.deleteImage(oldPost.getImageUrl());
        postRepository.deletePost(id);
    }

    @Override
    public void updateLikesCount(UUID postId, boolean like) {
        postRepository.updateLikesCount(postId, like);
    }
}
