package webservice.webservicepractice.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import webservice.webservicepractice.domain.user.Role;

/**
 * 스프링 시큐리티 설정 정보 클래스
 */
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {


    /**
     * 소셜 로그인 성공 후 추가로 진행하는 기능들을 따로 주입받아서 사용함.
     */
    private final CustomOAuth2UserService customOAuth2UserService;


    /**
     * <메서드 설명>
     *  SecurityFilterChain 을 만들어서 빈으로 등록하는 과정
     *
     * http.csrf().disable().headers().frameOptions().disable()
     *  ??h2-console 화면을 사용하기 위해서 해당 옵션을 disable
     *
     * .and()
     * and 로 다음 명령 수행 가능
     * .authorizeRequests()
     * URL 별 권한 관리 설정 옵션의 시작점이다. 이녀석을 선언해야 antMatchers 옵션 사용 가능
     *
     * .anyRequest().authenticated()
     * 설정된 값들 이외의 나머지 URL .authenticated() 를 추가하여 나머지 URL 들은 모두
     * 인증된 사용자들에게만 허용함(로그인한 사용자)
     *
     * .oauth2Login.userInfoEndpoint().userService();
     * OAuth 2 로그인 기능에 대한 여러 설정의 진입점, 진입 정보가 어디 있는지 알려주는 거 같음
     *
     * .userInfoEndPoint()
     * OAuth 2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들들 담당
     *
     * .userService(customOAuth2UserService);
     * 소셜 로그인 성공 후 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록한다.
     * 리소스 서버(소셜 서비스 ex)구글,네이버,카카오) 에서 사용자 정보를 가져온 상태에서
     * 추가로 진행하고자 하는 기능들을 명시한 클래스를 변수로 받음
     *
     */
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/","/css/**","/images/**","/js/**","/h2-console/**")
                .permitAll()
                .antMatchers("/api/v1/**", "/posts/**").hasRole(Role.USER.name())
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/") // 로그아웃 진입점
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

        return http.build();
    }
}
