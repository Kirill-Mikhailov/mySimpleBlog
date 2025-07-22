package ru.yandex.practicum.mysimpleblog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.mysimpleblog.dto.PostDto;
import ru.yandex.practicum.mysimpleblog.service.PostService;

import java.util.UUID;

@Controller
@RequestMapping("/posts/{id}")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public String getPost(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("post", postService.getPost(id));
        return "post";
    }

    @PostMapping("/like")
    public String updateLikesCount(@PathVariable("id") UUID postId,
                                   @RequestParam("like") boolean like) {
        postService.updateLikesCount(postId, like);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/edit")
    public String editPost(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("post", postService.getPost(id));
        return "add-post";
    }

    @PostMapping
    public String editPost(@PathVariable("id") UUID id,
                           @RequestParam("title") String title,
                           @RequestParam("text") String text,
                           @RequestParam(name = "image", required = false) MultipartFile image,
                           @RequestParam("tags") String tags) {
        PostDto postDto = PostDto.builder()
                .id(id)
                .title(title)
                .text(text)
                .build();
        postDto.setTags(tags);

        postService.editPost(postDto, image);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/delete")
    public String deletePost(@PathVariable("id") UUID id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }
}
