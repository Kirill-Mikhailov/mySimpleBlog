package ru.yandex.practicum.mysimpleblog.repository;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ImageRepository {

    Resource getImageBytes(UUID postId);

    String addImage(MultipartFile image);

    void deleteImage(String imageUrl);
}
