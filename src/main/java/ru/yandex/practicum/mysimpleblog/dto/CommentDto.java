package ru.yandex.practicum.mysimpleblog.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class CommentDto {

    UUID id;
    UUID postId;
    String content;
    Instant lastChangeTimestamp;
}
