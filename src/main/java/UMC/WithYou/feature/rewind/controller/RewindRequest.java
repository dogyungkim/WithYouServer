package UMC.WithYou.feature.rewind.controller;

import UMC.WithYou.common.validation.annotation.ExistQnaId;
import UMC.WithYou.common.validation.annotation.ExistQuestionId;
import UMC.WithYou.feature.rewind.domain.Mood;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.Getter;

import java.util.List;

public class RewindRequest {

    @Getter
    public static class CreateRewindDto {
        @Min(1)
        @Schema(description = "여행 일자", example = "1")
        Integer day;
        @Schema(description = "MVP 투표 대상 멤버 ID", example = "10")
        Long mvpCandidateId;
        @Schema(description = "오늘의 기분", example = "SAD")
        Mood mood;
        @Valid
        @Schema(description = "Q&A 목록")
        List<CreateRewindQnaDto> qnaList;
        @Schema(description = "오늘의 한마디", example = "좋은 날이었습니다.")
        String comment;
    }

    @Getter
    public static class CreateRewindQnaDto {
        @ExistQuestionId
        @Schema(description = "질문 ID", example = "456")
        Long questionId;
        @Schema(description = "질문 답변", example = "기분 좋았어요.")
        String answer;
    }

    @Getter
    public static class UpdateRewindDto {
        @Schema(description = "MVP 투표 대상 멤버 ID", example = "10")
        Long mvpCandidateId;
        @Schema(description = "오늘의 기분", example = "SAD")
        Mood mood;
        @Schema(description = "Q&A 목록")
        List<UpdateRewindQnaDto> qnaList;
        @Schema(description = "오늘의 한마디", example = "좋은 날이었습니다.")
        String comment;
    }

    @Getter
    public static class UpdateRewindQnaDto {
        @ExistQnaId
        @Schema(description = "Q&A ID (질문 ID가 아닌 Q&A ID)", example = "456")
        Long qnaId;
        @Schema(description = "질문 답변", example = "기분 좋았어요.")
        String answer;
    }
}
