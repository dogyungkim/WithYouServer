package UMC.WithYou.feature.member.controller;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class MemberResponse {
    private String imageUrl;
    private String name;

    @Builder
    public MemberResponse(String imageUrl, String name) {
        this.imageUrl = imageUrl;
        this.name = name;
    }
}
