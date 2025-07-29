package UMC.WithYou.feature.travel.service;

import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.apiPayload.exception.handler.CommonErrorHandler;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.member.repository.MemberRepository;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.domain.Traveler;
import UMC.WithYou.feature.travel.repository.TravelRepository;
import UMC.WithYou.infra.s3.S3FileType;
import UMC.WithYou.infra.s3.S3PreSignService;
import UMC.WithYou.infra.s3.S3Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Transactional
public class TravelService {
    private final TravelRepository travelRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final S3PreSignService s3PreSignService;

    public Long createTravel(Member member, String title, LocalDate startDate, LocalDate endDate, LocalDate localDate) {
        
        Travel travel = new Travel(member, title, startDate, endDate);

        travel.setTravelStatus(localDate);

        Traveler traveler = new Traveler(travel, member);
        travel.addTravelMember(traveler);
        
        return travelRepository.save(travel).getId();
    }

    public String createTravelWithMultipartFile(Member member, String title, LocalDate startDate, LocalDate endDate, LocalDate localDate, MultipartFile image) {
        Travel travel = new Travel(member, title, startDate, endDate);

        travel.setTravelStatus(localDate);

        Traveler traveler = new Traveler(travel, member);

        String imageUrl = s3Service.uploadImg(image);
        travel.setImageUrl(imageUrl);

        travel.addTravelMember(traveler);

        travelRepository.save(travel);
        return imageUrl;
    }

    public List<Travel> getTravels(Member member, LocalDate currentLocalDate) {
        List<Travel> travels = travelRepository.findTravelsByMemberWithFetch(member);

        for (Travel travel : travels) {
            travel.setTravelStatus(currentLocalDate);
        }
        // TODO : 이미지 추가 시 주석 해제
        // travels.forEach(t -> 
        //     t.setImageUrl(s3PreSignService.generatePresignedUrl(t.getId().toString(), S3FileType.BANNER))
        // );

        return travels;
    }

    public Long deleteTravel(Member member, Long travelId) {
        Travel travel = findTravelById(travelId);

        if (!travel.validateOwnership(member)) {
            throw new CommonErrorHandler(ErrorStatus.UNAUTHORIZED_ACCESS_TO_TRAVEL);
        }
        
        if (travel.getImageUrl() != null) {
            s3PreSignService.deleteFile(travel.getId().toString(), S3FileType.BANNER);
        }

        travelRepository.delete(travel);
        return travelId;
    }

    public void editTravel(Member member, Long travelId, String title,
                           LocalDate startDate, LocalDate endDate, LocalDate localDate) {
        Travel travel = findTravelById(travelId);

        validateTraveler(member, travel);
        travel.edit(title, startDate, endDate);
        travel.setTravelStatus(localDate);
    }

    public void editTravelWithImage(Member member, Long travelId, String title,
    LocalDate startDate, LocalDate endDate, LocalDate localDate) {
        Travel travel = findTravelById(travelId);
        validateTraveler(member, travel);

        travel.edit(title, startDate, endDate);
        travel.setTravelStatus(localDate);
    }

    public List<Member> getMembers(Member member, Long travelId) {
        Travel travel = findTravelById(travelId);

        validateTraveler(member, travel);

        return travel.getTravelMembers();
    }

    public Traveler join(Member member, String invitationCode) {
        Travel travel = travelRepository.findByInvitationCode(invitationCode).orElseThrow(
                () -> new CommonErrorHandler(ErrorStatus.INVITATION_CODE_NOT_FOUND)
        );

        Traveler traveler = new Traveler(travel, member);

        if (travel.isTraveler(member)) {
            return traveler;
        }

        travel.addTravelMember(traveler);
        member.addTraveler(traveler);
        return traveler;
    }

    public String getInvitationCode(Member member, Long travelId) {
        Travel travel = findTravelById(travelId);

        validateTraveler(member, travel);

        if (travel.hasInvitationCode()) {
            return travel.getInvitationCode();
        }

        String invitationCode = UUID.randomUUID().toString();

        while (travelRepository.findByInvitationCode(invitationCode).isPresent()) {
            invitationCode = UUID.randomUUID().toString();
        }
        travel.setInvitationCode(invitationCode);
        return invitationCode;

    }


    public void leave(Member member, Long travelId, Long memberId) {
        Travel travel = findTravelById(travelId);
        validateTraveler(member, travel);

        Member travelMember = memberRepository.findById(memberId).orElseThrow(
                ()->new CommonErrorHandler(ErrorStatus.MEMBER_NOT_FOUND)
        );
        travel.leave(travelMember);

    }





    private void validateTraveler(Member member, Travel travel) {
        if (!travel.isTraveler(member)) {
            throw new CommonErrorHandler(ErrorStatus.UNAUTHORIZED_ACCESS_TO_TRAVEL);
        }
    }

    public Travel findTravelById(Long travelId) {
        return travelRepository.findById(travelId).orElseThrow(
                () -> new CommonErrorHandler(ErrorStatus.TRAVEL_LOG_NOT_FOUND)
        );
    }
}


