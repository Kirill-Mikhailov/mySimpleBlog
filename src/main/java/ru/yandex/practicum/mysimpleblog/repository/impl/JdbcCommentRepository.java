package ru.yandex.practicum.mysimpleblog.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.mysimpleblog.model.Comment;
import ru.yandex.practicum.mysimpleblog.repository.CommentRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Comment addComment(Comment comment) {
        String sqlQuery = "insert into comments(id, post_id, text, last_change_timestamp) values(?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, comment.getId(), comment.getPostId(), comment.getText(), Instant.now());
        return getComment(comment.getId());
    }

    @Override
    public Comment editComment(Comment comment) {
        String sqlQuery = "update comments set text = ? where id = ?";
        jdbcTemplate.update(sqlQuery, comment.getText(), comment.getId());
        return getComment(comment.getId());
    }

    @Override
    public void deleteComment(UUID id) {
        jdbcTemplate.update("delete from comments WHERE id = ?", id);
    }

    @Override
    public List<Comment> getCommentsByPostIds(List<UUID> postIds) {
        String sqlQuery = "select id, post_id, text, last_change_timestamp from comments where post_id in (:postIds)";

        Map<String, Object> params = new HashMap<>();
        params.put("postIds", postIds);

        return namedParameterJdbcTemplate.query(sqlQuery, params, this::commentFromRow);
    }

    private Comment getComment(UUID id) {
        String sqlQuery =
                "select id, post_id, text, last_change_timestamp from comments where id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::commentFromRow, id);
    }

    private Comment commentFromRow(ResultSet rs, int rowNum) throws SQLException {
        return Comment.builder()
                .id(rs.getObject("id", UUID.class))
                .postId(rs.getObject("post_id", UUID.class))
                .text(rs.getString("text"))
                .lastChangeTimestamp(rs.getTimestamp("last_change_timestamp").toInstant())
                .build();
    }
}
