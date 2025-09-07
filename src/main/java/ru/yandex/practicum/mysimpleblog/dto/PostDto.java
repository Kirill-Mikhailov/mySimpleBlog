package ru.yandex.practicum.mysimpleblog.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.yandex.practicum.mysimpleblog.Utils.PREVIEW_SIZE;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostDto {

    @Builder.Default
    @EqualsAndHashCode.Include
    UUID id = UUID.randomUUID();
    String title;
    String imageUrl;
    String text;
    long likesCount;
    List<CommentDto> comments;
    List<String> tags;
    Instant lastChangeTimestamp;

    public String getTextPreview() {
        int delimiterIndex = text.indexOf("\r\n");
        if (delimiterIndex != -1)
            return text.substring(0, Math.min(PREVIEW_SIZE, delimiterIndex)) + "...";

        return text.length() > PREVIEW_SIZE
                ? text.substring(0, PREVIEW_SIZE) + "..."
                : text;
    }

    public List<String> getTextParts() {
        return Arrays.asList(text.split("\\r?\\n"));
    }

    public String getTagsAsText() {
        return CollectionUtils.isEmpty(tags)
                ? ""
                : tags.stream().map(tag -> "#" + tag).collect(Collectors.joining(" "));
    }

    public void setTags(String tags) {
        this.tags = Arrays.stream(tags.split(" "))
                .map(String::trim)
                .map(t -> t.replaceAll("#", ""))
                .toList();
    }
}
