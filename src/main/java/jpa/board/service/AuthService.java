package jpa.board.service;

import jpa.board.dto.OAuthDto;
import jpa.board.util.OAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final OAuthUtil oAuthUtil;

    public OAuthDto.KakaoTokenResponse oAuthLogin(String code) {
        return oAuthUtil.requestToken(code);
    }
}
