package com.bn1knb.newsblog.model.hateoas;

import com.bn1knb.newsblog.model.User;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import springfox.documentation.annotations.ApiIgnore;

import javax.xml.bind.annotation.XmlRootElement;

@Getter
@ApiIgnore
@XmlRootElement
public class UserResource extends ResourceSupport {
    private final User user;

    public UserResource(final User user) {
        this.user = user;
        if (user.getPosts() != null) {
            user.getPosts().forEach(PostResource::new);
        }
        Long userId = user.getId();
        add(new Link("http://localhost:80/proxy/users/" + userId).withSelfRel());
        add(new Link("http://localhost:80/proxy/users/" + userId + "/posts").withRel("posts"));
    }
}
