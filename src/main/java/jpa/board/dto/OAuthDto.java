package jpa.board.dto;

import lombok.Data;

public class OAuthDto {

    @Data
    public static class KakaoTokenResponse {
        private String access_token;
        private String token_type;
        private String refresh_token;
        private int expires_in;
        private String scope;
        private int refresh_token_expires_in;
    }

    @Data
    public static class KakaoProfileResponse {
        private String nickname;
        private String email;
        private String image;
    }
}
