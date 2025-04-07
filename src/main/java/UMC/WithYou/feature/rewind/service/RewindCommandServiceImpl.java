package UMC.WithYou.feature.rewind.service;

import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.apiPayload.exception.handler.CommonErrorHandler;
import UMC.WithYou.feature.rewind.domain.Rewind;
import UMC.WithYou.feature.rewind.domain.RewindQna;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.rewind.controller.RewindRequest;
import UMC.WithYou.feature.rewind.converter.RewindConverter;
import UMC.WithYou.feature.rewind.repository.RewindRepository;
import UMC.WithYou.feature.rewind.repository.RewindQnaRepository;
import UMC.WithYou.feature.rewind.repository.RewindQuestionRepository;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.domain.Traveler;
import UMC.WithYou.feature.travel.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RewindCommandServiceImpl implements RewindCommandService {

    private final RewindRepository rewindRepository;
    private final RewindQnaRepository rewindQnaRepository;
    private final RewindQuestionRepository rewindQuestionRepository;
    private final TravelRepository travelRepository;


    @Override
    public Rewind createRewind(Member member, Long travelId, RewindRequest.CreateRewindDto requestDto) {
        //travel check
        Travel travel = travelRepository.findById(travelId).orElseThrow(() -> new CommonErrorHandler(ErrorStatus.TRAVEL_LOG_NOT_FOUND));
        //check valid travel's day
        LocalDate startDate = travel.getStartDate();
        LocalDate endDate = travel.getEndDate();
        int travelDuration = (endDate.getDayOfYear() - startDate.getDayOfYear()) + 1;
        if (travelDuration < requestDto.getDay()) throw new CommonErrorHandler(ErrorStatus.TRAVEL_DAY_NOT_VALID);
        //traveler check
        Traveler traveler = travel.getTravelers().stream()
                .filter(t -> member.equals(t.getMember()))
                .findAny()
                .orElseThrow(() -> new CommonErrorHandler(ErrorStatus.MEMBER_NOT_IN_TRAVELER));
        //check rewind already exists
        Optional<Rewind> rewindOptional = rewindRepository.findByTravelAndWriterAndDay(travel, member, requestDto.getDay());
        rewindOptional.ifPresent(result -> {
            throw new CommonErrorHandler(ErrorStatus.REWIND_ALREADY_EXIST);
        });

        //변환&저장
        Rewind rewind = RewindConverter.toRewind(requestDto);
        rewind.setWriter(member);
        rewind.setTravel(travel);
        if(requestDto.getQnaList()!=null)
        requestDto.getQnaList().stream()
                .map(createRewindQnaDto -> {
                    RewindQna rewindQna = RewindQna.builder()
                            .answer(createRewindQnaDto.getAnswer())
                            .rewindQuestion(rewindQuestionRepository.findById(createRewindQnaDto.getQuestionId()).get())
                            .build();
                    rewindQna.setRewind(rewind);
                    return rewindQna;
                })
                .forEach(rewindQnaRepository::save);
        return rewindRepository.save(rewind);
    }

    @Override
    public Rewind updateRewindById(Member member, Long travelId, Long rewindId, RewindRequest.UpdateRewindDto requestDto) {
        //travel check
        Travel travel = travelRepository.findById(travelId).orElseThrow(() -> new CommonErrorHandler(ErrorStatus.TRAVEL_LOG_NOT_FOUND));
        Rewind rewind = rewindRepository.findById(rewindId).get();
        //writer check
        if(rewind.getWriter() != member) throw new CommonErrorHandler(ErrorStatus.NOT_VALID_WRITER);
        rewind.updateRewind(requestDto.getMvpCandidateId(), requestDto.getMood(), requestDto.getComment());
        requestDto.getQnaList().stream()
                .map(rewindQnaDto -> {
                    RewindQna rewindQna = rewindQnaRepository.findById(rewindQnaDto.getQnaId()).orElseThrow(() -> new CommonErrorHandler(ErrorStatus.QUESTION_NOT_FOUND));
                    rewindQna.updateRewindQna(rewindQnaDto.getAnswer());
                    rewindQna.setRewind(rewind);
                    return rewindQna;
                })
                .forEach(rewindQnaRepository::save);
        return rewindRepository.save(rewind);
    }

    @Override
    public void deleteRewindById(Member member, Long travelId, Long rewindId) {
        //travel check
        Travel travel = travelRepository.findById(travelId).orElseThrow(() -> new CommonErrorHandler(ErrorStatus.TRAVEL_LOG_NOT_FOUND));
        Rewind rewind = rewindRepository.findById(rewindId).get();
        //writer check
        if(rewind.getWriter() != member) throw new CommonErrorHandler(ErrorStatus.NOT_VALID_WRITER);
        rewindRepository.deleteById(rewind.getId());
    }
}
