package ru.yandex.practicum.mysimpleblog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

public class PostControllerTest extends AbstractPostControllerTest {

    @Test
    void getPost() throws Exception {
        mockMvc.perform(get("/posts/{id}", post.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(xpath("//table/tr").nodeCount(6))
                .andExpect(xpath("//table/tr[2]/td/h2/text()").string(post.getTitle()))
                .andExpect(xpath("//table/tr[2]/td/p[2]/form/span/text()").string(String.valueOf(post.getLikesCount())))
                .andExpect(xpath("//table/tr[2]/td/p[2]/form/span[2]/text()")
                        .string("комментарии " + post.getComments().size()))
                .andExpect(xpath("//table/tr[2]/td/p[3]/span/text()")
                        .string(post.getTags().stream().map(tag -> "#" + tag).collect(Collectors.joining(" ")) + " "))
                .andExpect(xpath("//table/tr[3]/td/text()").string(post.getText()))
                .andExpect(xpath("//table/tr[5]/td/form/span/text()")
                        .string(post.getComments().stream().findFirst().get().getText()));
    }

    @Test
    void addPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/posts")
                        .file(image)
                        .param("id", post.getId().toString())
                        .param("title", post.getTitle())
                        .param("tags", post.getTags().stream().map(tag -> "#" + tag).collect(Collectors.joining(" ")))
                        .param("text", post.getText()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }


    @Test
    void editPost() throws Exception {
        mockMvc.perform(post("/posts/{post_id}/edit", post.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(view().name("add-post"));
    }

    @Test
    void deletePost() throws Exception {
        mockMvc.perform(post("/posts/{post_id}/delete", post.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void updateLikesCount() throws Exception {
        mockMvc.perform(post("/posts/{post_id}/like", post.getId().toString())
                        .param("like", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + post.getId()));
    }
}
