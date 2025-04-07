package UMC.WithYou.feature.auth.domain;

public class AppleUserInfo implements UserInfo {
    private String sub;
    private String email;
    private String name;

    public AppleUserInfo(String sub, String email, String name) {
        this.sub = sub;
        this.email = email;
        this.name = name;
    }

    @Override
    public String getEmail(){
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getIdentifier() {
        return sub;
    }
}