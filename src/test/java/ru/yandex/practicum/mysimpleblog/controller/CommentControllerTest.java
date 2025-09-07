package ru.yandex.practicum.mysimpleblog.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.mysimpleblog.model.Comment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends AbstractPostControllerTest {

    @Test
    void addComment() throws Exception {

        mockMvc.perform(post("/posts/{id}/comments", post.getId().toString())
                        .param("text", post.getComments().stream().findFirst().get().getText()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + post.getId().toString()));
    }

    @Test
    void editComment() throws Exception {
        Comment comment = post.getComments().stream().findFirst().get();
        mockMvc.perform(post("/posts/{post_id}/comments/{comment_id}",
                        post.getId().toString(),
                        comment.getId().toString())
                        .param("comment_id", comment.getId().toString())
                        .param("text", "New comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + post.getId().toString()));
    }

    @Test
    void deleteComment() throws Exception {
        mockMvc.perform(post("/posts/{post_id}/comments/{comment_id}/delete",
                        post.getId().toString(),
                        post.getComments().stream().findFirst().get().getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + post.getId().toString()));
    }
}
