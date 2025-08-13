package UMC.WithYou.feature.travel.controller;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.domain.TravelStatus;
import UMC.WithYou.feature.travel.domain.Traveler;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class TravelResponseDTO {

    @Getter
    @AllArgsConstructor
    public static class CreateTravelResponseDTO {
        private String url;
    }

    @Getter
    public static class ThumbnailResponseDTO{
        private Long travelId;
        private String title;
        private LocalDate startDate;
        private LocalDate endDate;
        private TravelStatus status;
        private String imageUrl;
        public ThumbnailResponseDTO(Travel travel, String imageUrl){
            travelId = travel.getId();
            title = travel.getTitle();
            startDate = travel.getStartDate();
            endDate = travel.getEndDate();
            status = travel.getStatus();
            this.imageUrl = imageUrl;
        }
    }

    @Getter
    public static class TravelerResponseDTO{
        private Long memberId;
        private String name;
        private String imageUrl;
        public TravelerResponseDTO(Member member){
            this.memberId = member.getId();
            this.name = member.getName();;
            this.imageUrl = member.getProfileImageKey();
        }
    }

    @Getter
    public static class JoinResponseDTO{
        private Long memberId;
        private Long travelId;

        public JoinResponseDTO(Traveler traveler){
            this.memberId = traveler.getMember().getId();
            this.travelId = traveler.getTravel().getId();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class InvitationCodeResponseDTO{
        private Long travelId;
        private String invitationCode;
    }

    @Getter
    @AllArgsConstructor
    public static class DeletionResponseDTO{
        private Long travelId;
    }

    @Getter
    @AllArgsConstructor
    public static class LeaveResponseDTO{
        private Long travelId;
        private Long memberId;

    }

    @Getter
    @AllArgsConstructor
    public static class EditTravelResponseDTO{
        private String url;
    }

}
