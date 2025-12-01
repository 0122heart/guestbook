package com.github.heart0122.guestbook_backend.user.dto;

import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 프로필 응답 DTO
 * - 친구/본인: 모든 필드 포함
 * - 비친구: nickname, statusMessage만 포함
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    // 기본 정보 (모두에게 공개)
    private Long id;
    private String nickname;
    private String statusMsg;
    private Long relationId;

    // 방명록 (본인 또는 친구에게만 공개)
    private List<GuestbookDto> guestbooks;
}
