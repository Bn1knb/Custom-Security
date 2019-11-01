package com.bn1knb.newsblog.model.hateoas;

import com.bn1knb.newsblog.model.Post;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import springfox.documentation.annotations.ApiIgnore;

@Getter
@ApiIgnore
public class PostResource extends ResourceSupport {
    private final Post post;

    public PostResource(final Post post) {
        this.post = post;
        if (post.getComments() != null) {
            post.getComments().forEach(CommentResource::new);
        }
        Long postId = post.getId();
        add(new Link("http://localhost:80/proxy/posts/" + postId).withSelfRel());
        add(new Link("http://localhost:80/proxy/users/" + post.getUser().getId()).withRel("author"));
        add(new Link("http://localhost:80/proxy/posts/" + postId + "/comments").withRel("comments"));
    }
}
