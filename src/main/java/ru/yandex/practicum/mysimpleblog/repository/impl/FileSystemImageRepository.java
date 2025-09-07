package ru.yandex.practicum.mysimpleblog.repository.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.mysimpleblog.model.Post;
import ru.yandex.practicum.mysimpleblog.repository.ImageRepository;
import ru.yandex.practicum.mysimpleblog.repository.PostRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FileSystemImageRepository implements ImageRepository {

    private final PostRepository postRepository;

    @Value("${upload.images.dir}")
    String uploadDir;
    private Path imageStorageLocation;

    @PostConstruct
    public void init() {
        this.imageStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.imageStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать директорию хранения изображений: " + e.getMessage(), e);
        }
    }

    @Override
    public Resource getImageBytes(UUID postId) {
        Post post = postRepository.getPost(postId);
        String filename = post.getImageUrl();
        Path filePath = this.imageStorageLocation.resolve(filename);

        return new FileSystemResource(filePath.toFile());
    }

    @Override
    public String addImage(MultipartFile image) {
        try {
            String originalFileName = StringUtils.cleanPath(image.getOriginalFilename());
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID() + fileExtension;

            Path targetLocation = this.imageStorageLocation.resolve(uniqueFileName);
            Files.copy(image.getInputStream(), targetLocation);

            return uniqueFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Не удалось сохранить файл: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteImage(String imageUrl) {
        try {
            String cleanFileName = StringUtils.cleanPath(imageUrl);
            Path filePath = this.imageStorageLocation.resolve(cleanFileName).normalize();

            // защита от path traversal
            if (!filePath.startsWith(this.imageStorageLocation.normalize())) {
                throw new RuntimeException("Попытка доступа к файлу вне разрешенной директории");
            }

            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось удалить файл: " + e.getMessage(), e);
        }
    }
}
