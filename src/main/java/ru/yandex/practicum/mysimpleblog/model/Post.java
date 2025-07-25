package ru.yandex.practicum.mysimpleblog.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {

    @EqualsAndHashCode.Include
    UUID id;
    String title;
    String imageUrl;
    String text;
    long likesCount;
    List<Comment> comments;
    List<String> tags;
    Instant lastChangeTimestamp;
}
