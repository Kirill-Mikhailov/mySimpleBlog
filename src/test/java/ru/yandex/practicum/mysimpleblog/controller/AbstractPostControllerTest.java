package ru.yandex.practicum.mysimpleblog.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.mysimpleblog.model.Comment;
import ru.yandex.practicum.mysimpleblog.model.Post;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public abstract class AbstractPostControllerTest {

    @Autowired
    protected WebApplicationContext webApplicationContext;
    protected MockMvc mockMvc;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected static Post post;
    protected static MockMultipartFile image;

    @BeforeAll
    static void beforeAll() {
        UUID postId = UUID.randomUUID();

        Comment comment = Comment.builder()
                .id(UUID.randomUUID())
                .postId(postId)
                .text("Текст комментария")
                .lastChangeTimestamp(Instant.now())
                .build();

        post = Post.builder()
                .id(postId)
                .title("Название поста")
                .imageUrl(UUID.randomUUID() + ".jpg")
                .text("Текст поста")
                .likesCount(5)
                .comments(List.of(comment))
                .tags(List.of("тег"))
                .lastChangeTimestamp(Instant.now())
                .build();

        image = new MockMultipartFile(
                "image",
                "image.jpg",
                "image/jpeg",
                "data".getBytes());
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        jdbcTemplate.execute("delete from comments");
        jdbcTemplate.execute("delete from tags");
        jdbcTemplate.execute("delete from posts");

        String insertPostSqlQuery = """
                insert into posts (id, title, image_url, text, likes_count, last_change_timestamp) 
                values (?, ?, ?, ?, ?, ?)""";

        jdbcTemplate.update(
                insertPostSqlQuery,
                post.getId(),
                post.getTitle(),
                post.getImageUrl(),
                post.getText(),
                post.getLikesCount(),
                post.getLastChangeTimestamp());

        post.getTags().forEach(
                tag -> jdbcTemplate.update("insert into tags (post_id, tag) values (?, ?)", post.getId(), tag));

        post.getComments().forEach(comment -> jdbcTemplate.update(
                "insert into comments (id, post_id, text, last_change_timestamp) values (?, ?, ?, ?)",
                comment.getId(), comment.getPostId(), comment.getText(), comment.getLastChangeTimestamp()));
    }
}
