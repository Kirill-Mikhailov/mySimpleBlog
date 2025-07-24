package ru.yandex.practicum.mysimpleblog.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.mysimpleblog.model.Comment;
import ru.yandex.practicum.mysimpleblog.model.Post;
import ru.yandex.practicum.mysimpleblog.repository.CommentRepository;
import ru.yandex.practicum.mysimpleblog.repository.PostRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcPostRepositoryImpl implements PostRepository {

    private final JdbcTemplate jdbcTemplate;
    private final CommentRepository commentRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Post getPost(UUID id) {
        String sqlQuery =
                "select id, title, image_url, text, likes_count, last_change_timestamp from posts where id = ?";
        Post post = jdbcTemplate.queryForObject(sqlQuery, this::postFromRow, id);

        post.setComments(commentRepository.getCommentsByPostIds(List.of(id)));

        List<String> tags = getTagsByPostIds(List.of(id)).get(id);
        post.setTags(tags);

        return post;
    }

    @Override
    public Page<Post> getAll(String search, int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by("last_change_timestamp").descending());

        Map<String, Object> params = new HashMap<>();
        params.put("limit", pageable.getPageSize());
        params.put("offset", pageable.getOffset());

         String sqlQuery = """
                select distinct p.id, p.title, p.image_url, p.text, p.likes_count, p.last_change_timestamp
                from posts as p""";

         String condition = "";

        if (StringUtils.hasText(search)) {
            condition = " join tags t on t.post_id = p.id where t.tag ilike :search";

            params.put("search", "%" + search + "%");
        }

        sqlQuery += condition;

        long totalCountOfPosts = namedParameterJdbcTemplate.queryForObject(
                "select count(*) from (" + sqlQuery + ") as all_rows",
                params,
                Long.class);

        sqlQuery += " order by p.last_change_timestamp desc limit :limit offset :offset";

        List<Post> posts = namedParameterJdbcTemplate.query(sqlQuery, params, this::postFromRow);

        List<UUID> postIds = posts.stream().map(Post::getId).toList();

        Map<UUID, List<String>> tagsMap = getTagsByPostIds(postIds);

        Map<UUID, List<Comment>> commentsMap = commentRepository.getCommentsByPostIds(postIds).stream()
                .collect(Collectors.groupingBy(Comment::getPostId));
        commentsMap.values()
                .forEach(list -> list.sort(Comparator.comparing(Comment::getLastChangeTimestamp).reversed()));

        posts.forEach(p -> {
            p.setTags(tagsMap.getOrDefault(p.getId(), new ArrayList<>()));
            p.setComments(commentsMap.getOrDefault(p.getId(), new ArrayList<>()));
        });

        return new PageImpl<>(posts, pageable, totalCountOfPosts);
    }

    @Override
    public Post addPost(Post post) {
        String sqlQuery = "insert into posts(id, title, image_url, text, last_change_timestamp) values(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, post.getId(), post.getTitle(), post.getImageUrl(), post.getText(), Instant.now());
        updateTags(post.getId(), post.getTags());
        return getPost(post.getId());
    }

    @Override
    public Post editPost(Post post) {
        String sqlQuery = "update posts set title = ?, image_url = ?, text = ? where id = ?";
        jdbcTemplate.update(sqlQuery, post.getTitle(), post.getImageUrl(), post.getText(), post.getId());
        updateTags(post.getId(), post.getTags());
        return getPost(post.getId());
    }

    @Override
    public void deletePost(UUID id) {
        jdbcTemplate.update("delete from posts WHERE id = ?", id);
    }

    @Override
    public void updateLikesCount(UUID postId, boolean like) {
        jdbcTemplate.update("update posts set likes_count = likes_count + ? where id = ?",
                like ? 1 : -1,
                postId);
    }

    private Map<UUID, List<String>> getTagsByPostIds(List<UUID> postIds) {
        String sqlQuery = "select post_id, tag from tags where post_id in (:postIds)";

        Map<String, Object> params = new HashMap<>();
        params.put("postIds", postIds);

        return namedParameterJdbcTemplate.query(
                sqlQuery,
                params,
                (rs, rowNum) -> Map.entry(
                        rs.getObject("post_id", UUID.class),
                        rs.getString("tag")
                ))
                .stream()
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
    }

    private void updateTags(UUID postId, List<String> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return;
        }
        jdbcTemplate.update("delete from tags where post_id = ?", postId);

        tags.forEach(tag -> jdbcTemplate.update("insert into tags(post_id, tag) values(?, ?)", postId, tag));
    }

    private Post postFromRow(ResultSet rs, int rowNum) throws SQLException {
        return Post.builder()
                .id(rs.getObject("id", UUID.class))
                .title(rs.getString("title"))
                .imageUrl(rs.getString("image_url"))
                .text(rs.getString("text"))
                .likesCount(rs.getLong("likes_count"))
                .lastChangeTimestamp(rs.getTimestamp("last_change_timestamp").toInstant())
                .build();
    }
}
