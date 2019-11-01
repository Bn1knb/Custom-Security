package com.bn1knb.newsblog.model.hateoas;

import com.bn1knb.newsblog.model.Comment;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import springfox.documentation.annotations.ApiIgnore;

@Getter
@ApiIgnore
public class CommentResource extends ResourceSupport {
    private final Comment comment;

    public CommentResource(final Comment comment) {
        this.comment = comment;
        Long commentId = comment.getId();
        add(new Link("http://localhost:80/proxy/posts/" + comment.getPost().getId() + "/comments/" + commentId).withSelfRel());
        add(new Link("http://localhost:80/proxy/users/" + comment.getUser().getId()).withRel("author"));
        add(new Link("http://localhost:80/proxy/posts/" + comment.getPost().getId()).withRel("post"));
    }
}
