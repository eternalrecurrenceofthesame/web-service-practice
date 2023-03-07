package webservice.webservicepractice.config.auth.dto;

import lombok.Data;
import webservice.webservicepractice.domain.user.User;

import java.io.Serializable;

@Data
public class SessionUser implements Serializable {

    /**
     * User 를 직접 세션 값으로 등록하지 않고 SessionUser 클래스를 만들고
     * 직렬화 해서 사용하는 이유는 엔티티에는 다른 엔티티와의 관계가 생길 수 있기 떄문이다
     *
     * 직렬화 대상에는 자식들 까지 포함되니 성능 이슈 부수 효과가 발생할 확률이 높다.
     * ex) @OneToMany, @ManyToOne
     */
    private String name;
    private String email;
    private String picture;
    
    public SessionUser(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        
    }
}
