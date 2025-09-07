package ru.yandex.practicum.mysimpleblog.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.mysimpleblog.WebConfiguration;
import ru.yandex.practicum.mysimpleblog.model.Comment;
import ru.yandex.practicum.mysimpleblog.model.Post;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@SpringJUnitConfig(classes = WebConfiguration.class)
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application.properties")
public class AbstractPostRepositoryTest {

    @Autowired
    protected WebApplicationContext webApplicationContext;
    @Autowired
    protected PostRepository postRepository;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected static Post post1;
    protected static Post post2;

    @BeforeAll
    static void beforeAll() {
        UUID postId1 = UUID.randomUUID();

        Comment comment1 = Comment.builder()
                .id(UUID.randomUUID())
                .postId(postId1)
                .text("Текст комментария 1")
                .lastChangeTimestamp(Instant.now())
                .build();

        post1 = Post.builder()
                .id(postId1)
                .title("Название поста 1")
                .imageUrl(UUID.randomUUID() + ".jpg")
                .text("Текст поста 1")
                .likesCount(5)
                .comments(List.of(comment1))
                .tags(List.of("тег"))
                .lastChangeTimestamp(Instant.now())
                .build();

        UUID postId2 = UUID.randomUUID();

        Comment comment2 = Comment.builder()
                .id(UUID.randomUUID())
                .postId(postId2)
                .text("Текст комментария 2")
                .lastChangeTimestamp(Instant.now())
                .build();

        post2 = Post.builder()
                .id(postId2)
                .title("Название поста 2")
                .imageUrl(UUID.randomUUID() + ".jpg")
                .text("Текст поста 2")
                .likesCount(5)
                .comments(List.of(comment2))
                .tags(List.of("кек"))
                .lastChangeTimestamp(Instant.now())
                .build();
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("delete from comments");
        jdbcTemplate.execute("delete from tags");
        jdbcTemplate.execute("delete from posts");

        String insertPostSqlQuery = """
                insert into posts (id, title, image_url, text, likes_count, last_change_timestamp) 
                values (?, ?, ?, ?, ?, ?)""";

        jdbcTemplate.update(
                insertPostSqlQuery,
                post1.getId(),
                post1.getTitle(),
                post1.getImageUrl(),
                post1.getText(),
                post1.getLikesCount(),
                post1.getLastChangeTimestamp());

        post1.getTags().forEach(
                tag -> jdbcTemplate.update("insert into tags (post_id, tag) values (?, ?)", post1.getId(), tag));

        post1.getComments().forEach(comment -> jdbcTemplate.update(
                "insert into comments (id, post_id, text, last_change_timestamp) values (?, ?, ?, ?)",
                comment.getId(), comment.getPostId(), comment.getText(), comment.getLastChangeTimestamp()));

        jdbcTemplate.update(
                insertPostSqlQuery,
                post2.getId(),
                post2.getTitle(),
                post2.getImageUrl(),
                post2.getText(),
                post2.getLikesCount(),
                post2.getLastChangeTimestamp());

        post2.getTags().forEach(
                tag -> jdbcTemplate.update("insert into tags (post_id, tag) values (?, ?)", post2.getId(), tag));

        post2.getComments().forEach(comment -> jdbcTemplate.update(
                "insert into comments (id, post_id, text, last_change_timestamp) values (?, ?, ?, ?)",
                comment.getId(), comment.getPostId(), comment.getText(), comment.getLastChangeTimestamp()));
    }
}
