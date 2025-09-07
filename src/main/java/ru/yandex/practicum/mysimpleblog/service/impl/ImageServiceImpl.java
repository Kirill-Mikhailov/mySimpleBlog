package ru.yandex.practicum.mysimpleblog.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.mysimpleblog.repository.ImageRepository;
import ru.yandex.practicum.mysimpleblog.service.ImageService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageServiceImpl implements ImageService {

    ImageRepository imageRepository;

    @Override
    public Resource getImageBytes(UUID postId) {
        return imageRepository.getImageBytes(postId);
    }

    @Override
    public String addImage(MultipartFile image) {
        return imageRepository.addImage(image);
    }

    @Override
    public void deleteImage(String imageUrl) {
        imageRepository.deleteImage(imageUrl);
    }
}
