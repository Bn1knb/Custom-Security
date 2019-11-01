package com.bn1knb.newsblog.controller.posts;

import com.bn1knb.newsblog.dto.CommentDto;
import com.bn1knb.newsblog.dto.PostDto;
import com.bn1knb.newsblog.model.Comment;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.hateoas.CommentResource;
import com.bn1knb.newsblog.model.hateoas.PostResource;
import com.bn1knb.newsblog.service.comment.CommentService;
import com.bn1knb.newsblog.service.post.PostService;
import com.bn1knb.newsblog.utills.TokenValueUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.Map;

@RequestMapping("/posts")
@RestController
public class PostsController {
    private static final String AUTH_TOKEN_COOKIE_NAME = "auth_token";

    private final PostService postService;
    private final CommentService commentService;

    @Autowired
    public PostsController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<PostResource> post(@Valid @RequestBody PostDto postDto,
                                             @RequestParam(value = "file", required = false) MultipartFile file,
                                             @CookieValue(AUTH_TOKEN_COOKIE_NAME) String authToken) throws IOException {

        String username = TokenValueUtils.decrypt(authToken);
        Post published = postService.save(postDto, username , file);

        URI location =  ServletUriComponentsBuilder
                .fromHttpUrl("http://localhost:80/proxy")
                .path("/posts/{postId}")
                .buildAndExpand(published.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new PostResource(published));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<Resources<PostResource>> getAllPosts(@PageableDefault(size = 5) Pageable pageable) {

        Link selfLink = new Link("http://localhost:80/proxy/posts/")
                .withSelfRel();

        Resources<PostResource> resources = new Resources<>(postService.findAllPerPage(pageable), selfLink);

        return ResponseEntity
                .ok(resources);
    }

    @GetMapping(value = "/{postId}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<PostResource> getPostById(@PathVariable("postId") Long postId) {
        Post existing = postService.findPostById(postId);

        return ResponseEntity
                .ok(new PostResource(existing));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Post> deletePost(@PathVariable("postId") Long postToDeleteId,
                                           @CookieValue(AUTH_TOKEN_COOKIE_NAME) String authToken) throws AccessDeniedException {

        String username = TokenValueUtils.decrypt(authToken);
        postService.delete(postToDeleteId, postService.hasPermissionToDelete(postToDeleteId, username));

        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping(value = "/{postId}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<PostResource> editPost(@Valid @RequestBody PostDto editedPost,
                                                 @PathVariable("postId") Long postId,
                                                 @CookieValue(AUTH_TOKEN_COOKIE_NAME) String authToken) {

        String username = TokenValueUtils.decrypt(authToken);
        Post post = postService.update(postId, editedPost, username);

        return ResponseEntity
                .ok(new PostResource(post));
    }

    @PatchMapping(value = "/{postId}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<PostResource> patchPost(@PathVariable("postId") Long postId,
                                                  @RequestBody Map<String, String> fields) {
        Post post = postService.patch(fields, postId);

        return ResponseEntity
                .ok(new PostResource(post));
    }

    @PostMapping(path = "/{postId}/comments", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<CommentResource> comment(@Valid @RequestBody CommentDto commentDto,
                                                   @RequestParam(value = "file", required = false) MultipartFile file,
                                                   @PathVariable("postId") Long postId,
                                                   @CookieValue(AUTH_TOKEN_COOKIE_NAME) String authToken) throws IOException {

        String username = TokenValueUtils.decrypt(authToken);
        Comment published = commentService.save(commentDto, file, postId, username);

        URI location = ServletUriComponentsBuilder
                .fromHttpUrl("http://localhost:80/proxy")
                .path("/posts/{postId}/{commentId}")
                .buildAndExpand(postId, published.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new CommentResource(published));
    }

    @GetMapping(path = "/{postId}/comments", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<Resources<CommentResource>> getAllCommentsOfPost(@PathVariable("postId") Long postId,
                                                                           @PageableDefault(size = 5) Pageable pageable) {
        Link selfLink = new Link("http://localhost:80/proxy/posts/"
                + postId
                + "/comments")
                .withSelfRel();

        Resources<CommentResource> resources = new Resources<>(
                commentService.findCommentOfTheCurrentPost(postId, pageable), selfLink);

        return ResponseEntity
                .ok(resources);
    }
}
