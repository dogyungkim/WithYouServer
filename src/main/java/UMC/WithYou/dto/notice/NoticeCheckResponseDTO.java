package UMC.WithYou.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class NoticeCheckResponseDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultDto{
        Long noticeId;
        LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShortResponseDto{
        Long noticeId;
        String url;       //공지 쓴 멤버 사진
        String name;      //공지 쓴 멤버 이름
        String content;
        int checkNum;
        boolean isChecked;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShortDto{
        String url;       //공지 쓴 멤버 사진
        String name;      //공지 쓴 멤버 이름
    }
}
