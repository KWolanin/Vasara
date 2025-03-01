package com.kai.Vasara.controller.comment;

import com.kai.Vasara.model.comment.CommentDTO;
import com.kai.Vasara.service.comment.CommentService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestBody CommentRequest commentRequest) {
        commentService.addComment(commentRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{chapterId}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable long chapterId) {
        return ResponseEntity.ok(commentService.getCommentsForChapter(chapterId));
    }

    @GetMapping("/perms/{chapterId}")
    public ResponseEntity<CommentService.CommentPermission> getPermissions(@PathVariable long chapterId) {
        return ResponseEntity.ok(commentService.getPermissions(chapterId));
    }

    @Getter
    @Setter
    public static class CommentRequest {
        private Long storyId;
        private Long chapterId;
        private String content;
        private Long parentId;
        private String name;
        private String email;
    }

}
