package jpa.board.service;

import io.jsonwebtoken.JwtException;
import jpa.board.config.JwtTokenProvider;
import jpa.board.domain.RoleType;
import jpa.board.dto.JwtTokenRequest;
import jpa.board.dto.JwtTokenResponse;
import jpa.board.dto.OAuthDto;
import jpa.board.dto.SignInDto;
import jpa.board.util.OAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final OAuthUtil oAuthUtil;

    public JwtTokenResponse signIn(SignInDto signInDto) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(signInDto.getEmail(), signInDto.getPassword());

        Authentication authenticate
                = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authenticate);
    }

    public JwtTokenResponse reissue(JwtTokenRequest jwtTokenRequest) {
        String memberEmail = jwtTokenProvider.getMemberEmail(jwtTokenRequest.getRefreshToken());

        if (!jwtTokenProvider.validateRefreshToken(memberEmail, jwtTokenRequest.getRefreshToken())) {
            jwtTokenProvider.invalidRefreshToken(memberEmail);
            throw new JwtException("Invalid Refresh token");
        }

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(RoleType.USER.name()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(memberEmail, null, authorities);
        return jwtTokenProvider.generateToken(authentication);
    }

    public void logout(JwtTokenRequest jwtTokenRequest) {
        String memberEmail = jwtTokenProvider.getMemberEmail(jwtTokenRequest.getRefreshToken());
        jwtTokenProvider.invalidRefreshToken(memberEmail);
    }

    public OAuthDto.KakaoProfileResponse oAuthLogin(String code) {
        OAuthDto.KakaoTokenResponse token = oAuthUtil.requestToken(code);
        return oAuthUtil.requestProfile(token);
    }
}
