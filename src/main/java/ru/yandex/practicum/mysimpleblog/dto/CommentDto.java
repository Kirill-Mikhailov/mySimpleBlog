package ru.yandex.practicum.mysimpleblog.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommentDto {

    @Builder.Default
    @EqualsAndHashCode.Include
    UUID id = UUID.randomUUID();
    UUID postId;
    String text;
    Instant lastChangeTimestamp;
}
