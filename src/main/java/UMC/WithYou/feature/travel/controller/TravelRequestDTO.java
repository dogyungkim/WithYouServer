package UMC.WithYou.feature.travel.controller;

import java.time.LocalDate;
import lombok.Getter;

public class TravelRequestDTO {

    @Getter
    public static class ConfigurationRequestDTO {
        private String title;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalDate localDate;
    }

    @Getter
    public static class JoinRequestDTO{
        private String invitationCode;
    }
}
