package UMC.WithYou.feature.auth.controller;

import lombok.Data;

import java.util.List;

@Data
public class ApplePublicKeyResponse {
    private List<AppleKey> keys;
    @Data
    public static class AppleKey {
        private String kty;
        private String kid;
        private String use;
        private String alg;
        private String n;
        private String e;
    }
}