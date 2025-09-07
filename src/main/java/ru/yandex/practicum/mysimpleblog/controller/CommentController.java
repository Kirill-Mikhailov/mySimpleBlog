package ru.yandex.practicum.mysimpleblog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.mysimpleblog.dto.CommentDto;
import ru.yandex.practicum.mysimpleblog.service.CommentService;

import java.util.UUID;

@Controller
@RequestMapping("/posts/{id}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public String addComment(@PathVariable("id") UUID postId,
                             @RequestParam("text") String text) {

        CommentDto commentDto = CommentDto.builder()
                .postId(postId)
                .text(text)
                .build();
        commentService.addComment(commentDto);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{commentId}")
    public String editComment(@PathVariable("id") UUID postId,
                              @PathVariable("commentId") UUID commentId,
                              @RequestParam("text") String text) {
        CommentDto commentDto = CommentDto.builder()
                .id(commentId)
                .postId(postId)
                .text(text)
                .build();
        commentService.editComment(commentDto);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable("id") UUID postId,
                                @PathVariable("commentId") UUID commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/posts/" + postId;
    }
}
