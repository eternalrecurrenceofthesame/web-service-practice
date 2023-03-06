package webservice.webservicepractice.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import webservice.webservicepractice.domain.BaseTimeEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String author;


    /** 메서드 */
    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

    /** 게시글 생성 */
    @Builder(builderMethodName = "createPosts")
    private Posts(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }



}
