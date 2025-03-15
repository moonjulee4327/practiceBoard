package jpa.board.util;

import jpa.board.dto.OAuthDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OAuthUtil {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;
    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String grantType;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    public OAuthDto.KakaoTokenResponse requestToken(String code) {
        WebClient webClient = WebClient.create();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("client_secret", clientSecret);

        return webClient.post()
                .uri(tokenUri)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .bodyToMono(OAuthDto.KakaoTokenResponse.class)
                .block();
    }

    public OAuthDto.KakaoProfileResponse requestProfile(OAuthDto.KakaoTokenResponse token) {
        WebClient webClient = WebClient.create();

        return webClient.get()
                .uri(userInfoUri)
                .headers(headers -> {
                    headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
                    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccess_token());
                })
                .retrieve()
                .bodyToMono(OAuthDto.KakaoProfileResponse.class)
                .block();
    }
}
