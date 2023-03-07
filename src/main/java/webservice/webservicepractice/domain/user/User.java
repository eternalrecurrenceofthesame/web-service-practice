package webservice.webservicepractice.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String name;
    
    @Column
    private String email;
    
    @Column
    private String picture;
    
    @Enumerated(EnumType.STRING) // 디비에 Enum 값 저장시 String 으로 저장
    @Column
    private Role role;


    @Builder
    public User(Long id, String name, String email, String picture, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    /**
     * 유저 정보 수정
     */
    public User update(String name, String picture){
        this.name = name;
        this.picture = picture;

        return this;
    }

    /**
     * 권한 코드로 권한 확인 메서드
     */
    public String getRoleKey(){
        return this.role.getKey();
    }
}
