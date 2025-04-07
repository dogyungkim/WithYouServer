package UMC.WithYou.feature.rewind.service;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.rewind.domain.Rewind;
import UMC.WithYou.feature.rewind.domain.RewindQuestion;

import java.util.List;

public interface RewindQueryService {
    List<Rewind> retrieveRewindsInTravel(Member member, Long travelId, Integer day);
    Rewind retrieveRewindById(Member member, Long travelId, Long rewindId);
    boolean checkRewindIdExist(Long rewindId);
    List<RewindQuestion> retrieveAllRewindQuestions();
}
