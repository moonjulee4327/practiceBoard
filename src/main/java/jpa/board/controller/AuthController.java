package jpa.board.controller;

import jpa.board.dto.JwtTokenRequest;
import jpa.board.dto.JwtTokenResponse;
import jpa.board.dto.OAuthDto;
import jpa.board.dto.SignInDto;
import jpa.board.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/kakao")
    public ResponseEntity<OAuthDto.KakaoProfileResponse> kakaoLogin(@RequestParam("code") String code) {
        return ResponseEntity.ok(authService.oAuthLogin(code));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse> signIn(@RequestBody SignInDto signInDto) {
        JwtTokenResponse jwtTokenResponse = authService.signIn(signInDto);
        return ResponseEntity.ok(jwtTokenResponse);
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtTokenResponse> reissue(@RequestBody JwtTokenRequest jwtTokenRequest) {
        JwtTokenResponse jwtTokenResponse = authService.reissue(jwtTokenRequest);
        return ResponseEntity.ok(jwtTokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody JwtTokenRequest jwtTokenRequest) {
        authService.logout(jwtTokenRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
