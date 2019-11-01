package com.bn1knb.newsblog.dto;

import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto implements Serializable {

    @NotBlank
    private String headline;
    @Size(min = 10)
    private String content;

    @ApiModelProperty(hidden = true)
    public Post toPost(User author) {
        return Post
                .builder()
                .headline(headline)
                .content(content)
                .user(author)
                .build();
    }
}
