package UMC.WithYou.feature.travel.controller;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;

public class TravelRequestDTO {

    @Getter
    public static class EditTravelRequestDTO {
        private String title;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalDate localDate;
    }

    @Getter
    public static class JoinRequestDTO{
        private String invitationCode;
    }

    @Getter
    public static class CreatePodRequestDTO {
        @Length(max = 15)
        private String title;
        private LocalDate localDate;
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Getter
    public static class CreateTravelRequestDTO {
        private String title;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalDate localDate;
    }
    
}
