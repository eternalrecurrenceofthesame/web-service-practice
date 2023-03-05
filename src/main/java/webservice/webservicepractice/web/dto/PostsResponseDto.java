package webservice.webservicepractice.web.dto;

import lombok.Getter;
import webservice.webservicepractice.domain.posts.Posts;

@Getter
public class PostsResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;

    // 엔티티를 받고 값을 Dto 로 변환해서 반환하기.
    public PostsResponseDto(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }

}
