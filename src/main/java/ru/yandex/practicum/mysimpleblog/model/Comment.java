package ru.yandex.practicum.mysimpleblog.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {

    UUID id;
    UUID postId;
    String text;
    Instant lastChangeTimestamp;
}
