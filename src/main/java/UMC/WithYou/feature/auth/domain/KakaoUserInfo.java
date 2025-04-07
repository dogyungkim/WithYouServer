package UMC.WithYou.feature.auth.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

public class KakaoUserInfo implements UserInfo {
    private final Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = asMap(attributes.get("kakao_account"));
        Object email = kakaoAccount.get("email");
        return String.valueOf(email);
    }

    @Override
    public String getName() {
        Map<String, Object> kakaoAccount = asMap(attributes.get("kakao_account"));
        Object name = kakaoAccount.get("name");
        return String.valueOf(name);
    }

    @Override
    public String getIdentifier() {
        Object id = attributes.get("id");
        return String.valueOf(id);
    }

    private static Map<String, Object> asMap(Object obj) {
        return Optional.ofNullable(obj)
                .filter(o -> o instanceof Map<?, ?>)
                .map(o -> (Map<String, Object>) o)
                .orElseThrow(() -> new IllegalArgumentException("Not a map: " + obj));
    }
}

