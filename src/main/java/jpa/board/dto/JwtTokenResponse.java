package jpa.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwtTokenResponse {
    private String grantType;
    private String accessToken;
}
