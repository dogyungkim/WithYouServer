package UMC.WithYou.feature.rewind.service;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.rewind.controller.RewindRequest;
import UMC.WithYou.feature.rewind.domain.Rewind;

public interface RewindCommandService {
    Rewind createRewind(Member member, Long travelId, RewindRequest.CreateRewindDto requestDto);
    Rewind updateRewindById(Member member, Long travelId, Long rewindId, RewindRequest.UpdateRewindDto requestDto);
    void deleteRewindById(Member member, Long travelId, Long rewindId);
}
