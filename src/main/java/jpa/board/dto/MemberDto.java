package jpa.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class MemberDto {
    private Long no;

    private String name;

    private String nickname;

    private ZonedDateTime createdDate;
}