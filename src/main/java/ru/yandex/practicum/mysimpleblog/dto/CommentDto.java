package ru.yandex.practicum.mysimpleblog.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class CommentDto {

    @Builder.Default
    UUID id = UUID.randomUUID();
    UUID postId;
    String text;
    Instant lastChangeTimestamp;
}
