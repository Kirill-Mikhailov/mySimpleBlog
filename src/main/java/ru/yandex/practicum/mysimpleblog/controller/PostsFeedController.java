package ru.yandex.practicum.mysimpleblog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.mysimpleblog.dto.PostDto;
import ru.yandex.practicum.mysimpleblog.service.PostService;

@Controller
@RequiredArgsConstructor
public class PostsFeedController {

    private final PostService postService;

    @GetMapping
    public String postsRedirect() {
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String getAll(@RequestParam(name = "search", defaultValue = "") String search,
                         @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                         @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                         Model model) {

        model.addAttribute("search", search);

        Page<PostDto> postsPage = postService.getAll(search, pageSize, pageNumber);
        model.addAttribute("paging", postsPage);
        model.addAttribute("posts", postsPage.getContent());
        return "posts";
    }

    @GetMapping("/posts/add")
    public String addPost() {
        return "add-post";
    }

    @PostMapping("/posts")
    public String addPost(@RequestParam("title") String title,
                          @RequestParam("text") String text,
                          @RequestParam("image") MultipartFile image,
                          @RequestParam("tags") String tags) {

        PostDto postDto = PostDto.builder()
                .title(title)
                .text(text)
                .build();
        postDto.setTags(tags);

        postService.addPost(postDto, image);
        return "redirect:/posts";
    }
}
