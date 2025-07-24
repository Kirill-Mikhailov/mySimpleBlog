package ru.yandex.practicum.mysimpleblog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

public class PostsFeedControllerTest extends AbstractPostControllerTest {

    @Test
    void getAll() throws Exception {
        mockMvc.perform(get("/posts")
                        .param("search", post.getTags().stream().findFirst().get()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("paging"))
                .andExpect(model().attributeExists("search"))
                .andExpect(xpath("//table/tr[2]/td/h2/text()").string(post.getTitle()))
                .andExpect(xpath("//table/tr[2]/td/p[2]/text()").string(post.getText()));
    }

    @Test
    void addPostRedirect() throws Exception {
        mockMvc.perform(get("/posts/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-post"));
    }

    @Test
    void addPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/posts")
                        .file(image)
                        .param("title", post.getTitle())
                        .param("tags", post.getTags().stream().findFirst().get())
                        .param("text", post.getText()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }
}
