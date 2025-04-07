package UMC.WithYou.feature.rewind.converter;

import UMC.WithYou.feature.rewind.controller.RewindRequest;
import UMC.WithYou.feature.rewind.controller.RewindResponse;
import UMC.WithYou.feature.rewind.domain.Rewind;
import UMC.WithYou.feature.rewind.domain.RewindQna;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RewindConverter {

    public static Rewind toRewind(RewindRequest.CreateRewindDto requestDto) {
        return Rewind.builder()
                .day(requestDto.getDay())
                .mvpCandidateId(requestDto.getMvpCandidateId())
                .mood(requestDto.getMood())
                .rewindQnaList(new ArrayList<>())
                .comment(requestDto.getComment()).build();
    }

    public static RewindResponse.CreateRewindResultDto toCreateRewindResultDto(Rewind rewind) {
        return RewindResponse.CreateRewindResultDto.builder()
                .rewindId(rewind.getId())
                .createdAt(rewind.getCreatedAt()).build();
    }

    public static RewindResponse.RetrieveRewindResultDto toRetrieveRewindResultDto(Rewind rewind) {
        return RewindResponse.RetrieveRewindResultDto.builder()
                .rewindId(rewind.getId())
                .day(rewind.getDay())
                .writerId(rewind.getWriter().getId())
                .mvpCandidateId(rewind.getMvpCandidateId())
                .mood(rewind.getMood())
                .comment(rewind.getComment())
                .rewindQnaList(toRetrieveRewindQnaDtoList(rewind.getRewindQnaList()))
                .createdAt(rewind.getCreatedAt())
                .updatedAt(rewind.getUpdatedAt())
                .build();
    }

    public static List<RewindResponse.RetrieveRewindResultDto> toRetrieveRewindResultDtoList(List<Rewind> rewindList) {
        //day 필드를 기준으로 오름차순 정렬
        rewindList.sort(Comparator.comparing(Rewind::getDay));
        List<RewindResponse.RetrieveRewindResultDto> retrieveRewindResultDtoList = rewindList.stream()
                .map(RewindConverter::toRetrieveRewindResultDto).collect(Collectors.toList());
        return retrieveRewindResultDtoList;
    }

    public static RewindResponse.RetrieveRewindQnaDto toRetrieveRewindQnaDto(RewindQna rewindQna) {
        return RewindResponse.RetrieveRewindQnaDto.builder()
                .qnaId(rewindQna.getId())
                .questionId(rewindQna.getRewindQuestion().getId())
                .content(rewindQna.getRewindQuestion().getContent())
                .answer(rewindQna.getAnswer())
                .build();
    }

    public static List<RewindResponse.RetrieveRewindQnaDto> toRetrieveRewindQnaDtoList(List<RewindQna> rewindQnaList) {
        List<RewindResponse.RetrieveRewindQnaDto> retrieveRewindQnaList = rewindQnaList.stream()
                .map(RewindConverter::toRetrieveRewindQnaDto).collect(Collectors.toList());
        return retrieveRewindQnaList;
    }

    public static RewindResponse.UpdateRewindResultDto toUpdateRewindResultDto(Rewind rewind) {
        return RewindResponse.UpdateRewindResultDto.builder()
                .rewindId(rewind.getId())
                .createdAt(rewind.getCreatedAt())
                .updatedAt(rewind.getUpdatedAt())
                .build();
    }
}
