package UMC.WithYou.feature.rewind.service;

import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.apiPayload.exception.handler.CommonErrorHandler;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.rewind.domain.Rewind;
import UMC.WithYou.feature.rewind.domain.RewindQuestion;
import UMC.WithYou.feature.rewind.repository.RewindQuestionRepository;
import UMC.WithYou.feature.rewind.repository.RewindRepository;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.domain.Traveler;
import UMC.WithYou.feature.travel.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RewindQueryServiceImpl implements RewindQueryService{

    private final RewindRepository rewindRepository;
    private final TravelRepository travelRepository;
    private final RewindQuestionRepository rewindQuestionRepository;

    @Override
    public List<Rewind> retrieveRewindsInTravel(Member member, Long travelId, Integer day) {
        //travel check
        Travel travel = travelRepository.findById(travelId).orElseThrow(() -> new CommonErrorHandler(ErrorStatus.TRAVEL_LOG_NOT_FOUND));
        //traveler check
        Traveler traveler = travel.getTravelers().stream()
                .filter(t -> member.equals(t.getMember()))
                .findAny()
                .orElseThrow(() -> new CommonErrorHandler(ErrorStatus.MEMBER_NOT_IN_TRAVELER));
        if(day != null) {
            // check valid travel's day
            LocalDate startDate = travel.getStartDate();
            LocalDate endDate = travel.getEndDate();
            int travelDuration = (endDate.getDayOfYear() - startDate.getDayOfYear()) + 1;
            if (travelDuration < day) throw new CommonErrorHandler(ErrorStatus.TRAVEL_DAY_NOT_VALID);
           return rewindRepository.findAllByTravelAndDay(travel, day);
          }
        return rewindRepository.findAllByTravel(travel);
    }

    @Override
    public Rewind retrieveRewindById(Member member, Long travelId, Long rewindId) {
        //travel check
        Travel travel = travelRepository.findById(travelId).orElseThrow(() -> new CommonErrorHandler(ErrorStatus.TRAVEL_LOG_NOT_FOUND));
        // traveler check
        Traveler traveler = travel.getTravelers().stream()
                .filter(t -> member.equals(t.getMember()))
                .findAny()
                .orElseThrow(() -> new CommonErrorHandler(ErrorStatus.MEMBER_NOT_IN_TRAVELER));
        return rewindRepository.findById(rewindId).get();
    }

    @Override
    public boolean checkRewindIdExist(Long rewindId) {
        Optional<Rewind> rewind = rewindRepository.findById(rewindId);
        return rewind.isPresent();
    }

    @Override
    public List<RewindQuestion> retrieveAllRewindQuestions() {
        return rewindQuestionRepository.findAll();
    }
}
