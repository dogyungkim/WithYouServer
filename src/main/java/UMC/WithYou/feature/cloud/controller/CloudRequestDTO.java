package UMC.WithYou.feature.cloud.controller;

import UMC.WithYou.common.validation.annotation.ExistClouds;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CloudRequestDTO {

    @Getter
    public static class CloudJoinDto{
        LocalDate date;
        Long travelId;
    }

    @Getter
    public static class DeleteDto{
        @ExistClouds
        Long cloudId;
        List<String> files;
    }

}
