package ru.yandex.practicum.mysimpleblog.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Value
@Builder
public class PostDto {

    UUID id;
    String title;
    String imageUrl;
    String content;
    long likesCount;
    List<CommentDto> comments;
    List<String> tags;
    Instant lastChangeTimestamp;

    //TODO
    public String getTextPreview() {
        return null;
    }

    //TODO
    public List<String> getTextParts() {
        return null;
    }

    //TODO
    public String getTagsAsText() {
        return null;
    }
}
