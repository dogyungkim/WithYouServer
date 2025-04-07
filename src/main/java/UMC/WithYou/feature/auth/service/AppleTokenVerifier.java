package UMC.WithYou.feature.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import UMC.WithYou.feature.auth.controller.ApplePublicKeyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.security.KeyFactory;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleTokenVerifier {
    @Value("${apple.client-id}")
    private String clientId;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public DecodedJWT verifyToken(String token, String nonce) throws Exception {
        ApplePublicKeyResponse response = fetchApplePublicKeys();
        RSAPublicKey publicKey = getPublicKeyFromResponse(token, response);

        DecodedJWT jwt = decodeAndVerifyToken(token, publicKey);

        verifyNonce(jwt, nonce);
        verifyClientId(jwt, clientId);

        return jwt;
    }

    private RSAPublicKey getPublicKeyFromResponse(String token, ApplePublicKeyResponse response) throws Exception {
        ApplePublicKeyResponse.AppleKey matchingKey = findMatchingKey(token, response);
        return generatePublicKey(matchingKey);
    }

    private DecodedJWT decodeAndVerifyToken(String token, RSAPublicKey publicKey) {
        Algorithm algorithm = Algorithm.RSA256(publicKey, null);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("https://appleid.apple.com")
                .build();
        return verifier.verify(token);
    }

    private void verifyNonce(DecodedJWT jwt, String expectedNonce) {
        String tokenNonce = jwt.getClaim("nonce").asString();
        if (tokenNonce == null || !tokenNonce.equals(expectedNonce)) {
            throw new SecurityException("Nonce verification failed.");
        }
    }

    private void verifyClientId(DecodedJWT jwt, String expectedClientId) {
        String audience = jwt.getAudience().get(0);
        if (audience == null || !audience.equals(expectedClientId)) {
            throw new SecurityException("Client ID verification failed.");
        }
    }

    private ApplePublicKeyResponse fetchApplePublicKeys() throws Exception {
        URL url = new URL("https://appleid.apple.com/auth/keys");
        return restTemplate.getForObject(url.toURI(), ApplePublicKeyResponse.class);
    }

    private ApplePublicKeyResponse.AppleKey findMatchingKey(String token, ApplePublicKeyResponse response) throws Exception {
        String header = new String(Base64.getUrlDecoder().decode(token.split("\\.")[0]));
        Map<String, String> headerMap = objectMapper.readValue(header, new TypeReference<>() {});
        String kid = headerMap.get("kid");

        return response.getKeys().stream()
                .filter(key -> kid.equals(key.getKid()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Matching key not found in Apple's public keys"));
    }

    private RSAPublicKey generatePublicKey(ApplePublicKeyResponse.AppleKey key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
        byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());
        BigInteger modulus = new BigInteger(1, nBytes);
        BigInteger exponent = new BigInteger(1, eBytes);

        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) factory.generatePublic(spec);
    }
}