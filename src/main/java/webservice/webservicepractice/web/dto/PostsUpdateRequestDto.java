package webservice.webservicepractice.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostsUpdateRequestDto {

    private String title;
    private String content;

    @Builder(builderMethodName = "updatePosts")
    public PostsUpdateRequestDto(String title, String content){
        this.title = title;
        this.content = content;
    }
}
