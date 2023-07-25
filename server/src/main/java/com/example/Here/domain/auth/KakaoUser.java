package com.example.Here.domain.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class KakaoUser {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class KakaoAccount {

        private Profile profile;
        private String email;
        private boolean profile_nickname_needs_agreement;

        @JsonIgnoreProperties(ignoreUnknown = true)
        @Data
        public static class Profile {
            private String nickname;
        }
    }
}



