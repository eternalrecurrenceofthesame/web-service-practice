package webservice.webservicepractice.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import webservice.webservicepractice.domain.posts.Posts;

@Data // 타임리프 사용시
@NoArgsConstructor
public class PostsSaveRequestDto {

    private String title;
    private String content;
    private String author;





    @Builder
    public PostsSaveRequestDto(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Posts toEntity(){
        return Posts.createPosts()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
