package webservice.webservicepractice.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    /**
     * 스프링 시큐리티에서는 권한 코드에 항상 ROLE_이 앞에 있어야 한다.
     * 그래서 코드별 키 값을 ROLE_GUEST, ROLE_USER 로 만듦
     *
     * 디비에 저장되는 값은 GUEST, USER
     * enum 안을 키와 밸류 값으로 채워넣고 권한이 필요한 경우 키 값으로 권한을 확인한다.
     */

    GUEST("ROLE_GUEST", "손님"), USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;

}
