package webservice.webservicepractice.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    /**
     * 소셜 로그인으로 반환되는 값 중 email 을 통해 이미 생성된 사용자인지 처음 가입하는
     * 사용자인지 판단하기 위한 이메일 셀렉트 쿼리
     */
    @Query("select u.email from User u")
    Optional<User> findByEmail(String email);
}
