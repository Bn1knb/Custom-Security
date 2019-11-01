package com.bn1knb.newsblog.controller.comments;

import com.bn1knb.newsblog.dto.CommentDto;
import com.bn1knb.newsblog.model.Comment;
import com.bn1knb.newsblog.model.hateoas.CommentResource;
import com.bn1knb.newsblog.service.comment.CommentService;
import com.bn1knb.newsblog.utills.TokenValueUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("posts/{postId}/comments")
public class CommentsController {
    private static final String AUTH_TOKEN_COOKIE_NAME = "auth_token";

    private final CommentService commentService;


    @Autowired
    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResource> getCommentById(@PathVariable("commentId") Long commentId,
                                                          @PathVariable String postId) {

        Comment existing = commentService.findCommentById(commentId);
        return ResponseEntity
                .ok(new CommentResource(existing));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Comment> deleteComment(@PathVariable("commentId") Long commentId,
                                                 @CookieValue(AUTH_TOKEN_COOKIE_NAME) String authToken,
                                                 @PathVariable String postId) {

        String username = TokenValueUtils.decrypt(authToken);
        commentService.delete(commentId, commentService.hasPermissionToDelete(commentId, username));

        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResource> editComment(@PathVariable("commentId") Long commentId,
                                                       @Valid @RequestBody CommentDto editedComment,
                                                       @CookieValue(AUTH_TOKEN_COOKIE_NAME) String authToken,
                                                       @PathVariable String postId) {

        String username = TokenValueUtils.decrypt(authToken);
        Comment comment = commentService.update(commentId, editedComment, username);

        return ResponseEntity
                .ok(new CommentResource(comment));
    }
}
