package webservice.webservicepractice.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import webservice.webservicepractice.config.auth.dto.OAuthAttributes;
import webservice.webservicepractice.config.auth.dto.SessionUser;
import webservice.webservicepractice.domain.user.User;
import webservice.webservicepractice.domain.user.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository; // 유저 정보 저장소
    private final HttpSession httpSession; // 연결 세션

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        // 소셜 로그인 유저 꺼내기??
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /**
         * 로그인 진행 중인 서비스를 구분하는 코드, 유저 요청 값에서 어떤 소셜 로그인 사용하는 지 아이디 값으로 꺼냄
         * ex) 구글이냐 네이버냐를 구분하는 코드를 꺼냄??
         */
        String registrationId
                = userRequest.getClientRegistration().getRegistrationId();

        /**
         * OAuth 2 로그인 진행시 (소셜 로그인) 키가 되는 필드 값을 꺼냄
         * 구글의 경우 기본적으로 코드를 지원하지만 네이버 카카오 로그인시 사용함 184p
         */
        String userNameAttributeName
                = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        /**
         * OAuth2UserService 를 통해 가져온 OAuth2User 의 attribute 를 담은 클래스
         */
        OAuthAttributes attributes
                = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());


        /**
         * 세션에 사용자 정보를 저장하기 위한 Dto 클래스
         */
        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());

    }

    private User saveOrUpdate(OAuthAttributes attributes){
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
