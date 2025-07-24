package ru.yandex.practicum.mysimpleblog.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comment {

    @EqualsAndHashCode.Include
    UUID id;
    UUID postId;
    String text;
    Instant lastChangeTimestamp;
}
