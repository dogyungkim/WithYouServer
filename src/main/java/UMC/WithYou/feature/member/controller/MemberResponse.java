package UMC.WithYou.feature.member.controller;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class MemberResponse {
    private String profileImageKey;
    private String name;

    @Builder
    public MemberResponse(String profileImageKey, String name) {
        this.profileImageKey = profileImageKey;
        this.name = name;
    }
}
