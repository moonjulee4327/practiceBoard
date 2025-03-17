package jpa.board.controller;

import jpa.board.dto.OAuthDto;
import jpa.board.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/login/kakao")
    public ResponseEntity<OAuthDto.KakaoProfileResponse> kakaoLogin(@RequestParam("code") String code) {
        return ResponseEntity.ok(authService.oAuthLogin(code));
    }
}
