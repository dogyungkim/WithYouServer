package UMC.WithYou.feature.auth.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import UMC.WithYou.feature.member.domain.Member;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class UserPrincipal implements UserDetails {

    private transient final Member member;

    private transient Map<String, Object> attributes;

    private UserPrincipal(Member member) {
        this.member = member;
    }
    public static UserPrincipal create(Member member) {
        return new UserPrincipal(member);
    }
    public static UserPrincipal create(Member member, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(member);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(getRoleFromMemberType()));
    }

    private String getRoleFromMemberType() {
        return switch (this.member.getMemberType()) {
            case BASIC_USER -> "ROLE_BASIC_USER";
            case ADMINISTRATOR -> "ROLE_ADMINISTRATOR";
            default -> throw new IllegalArgumentException("Unsupported member type: " + this.member.getMemberType());
        };
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.member.getIdentifier();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
