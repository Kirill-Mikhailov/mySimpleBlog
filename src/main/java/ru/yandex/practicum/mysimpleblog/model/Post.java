package ru.yandex.practicum.mysimpleblog.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {

    UUID id;
    String title;
    String imageUrl;
    String content;
    long likesCount;
    List<Comment> comments;
    List<String> tags;
    Instant lastChangeTimestamp;
}
