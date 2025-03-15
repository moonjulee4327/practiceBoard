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
        private Long id;
        private String connected_at;
        private Properties properties;
        private KakaoAccount kakao_account;

        @Data
        public static class Properties {
            private String nickname;
        }

        @Data
        public static class KakaoAccount {
            private Boolean profile_nickname_needs_agreement;
            private Boolean profile_image_needs_agreement;
            private Profile profile;
            private Boolean has_email;
            private Boolean email_needs_agreement;
            private Boolean is_email_valid;
            private Boolean is_email_verified;
            private String email;

            @Data
            public static class Profile {
                private String nickname;
                private Boolean is_default_nickname;
                private Boolean is_default_image;
            }
        }
    }
}
