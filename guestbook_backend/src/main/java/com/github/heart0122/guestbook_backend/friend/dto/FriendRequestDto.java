
package com.github.heart0122.guestbook_backend.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FriendRequestDto {
    private Long requestId;
    private String sender;
    private LocalDateTime createdAt;  // 요청 생성 시간 추가
}