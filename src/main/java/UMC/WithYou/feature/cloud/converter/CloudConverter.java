package UMC.WithYou.feature.cloud.converter;

import UMC.WithYou.feature.cloud.controller.CloudRequestDTO;
import UMC.WithYou.feature.cloud.controller.CloudResponseDTO;
import UMC.WithYou.feature.cloud.domain.Cloud;
import UMC.WithYou.feature.cloud.domain.CloudMedia;
import UMC.WithYou.feature.travel.domain.Travel;

import java.time.LocalDate;
import java.util.List;

public class CloudConverter {
    public static CloudResponseDTO.ResultDto toResultDTO(Cloud cloud){
        return CloudResponseDTO.ResultDto.builder()
                .createdAt(cloud.getCreatedAt())
                .cloudId(cloud.getId())
                .build();
    }

    public static CloudMedia toMedia(CloudRequestDTO.CloudJoinDto request, Cloud cloud, String picture){
        return CloudMedia.builder()
                .date(request.getDate())
                .url(picture)
                //.url(Collections.singletonList(pictureList))
                .cloud(cloud)
                .build();
    }

    public static Cloud toChangeCloud(Travel travel){
        return Cloud.builder()
                .travel(travel)
                .build();
    }

    public static CloudResponseDTO.PictureDto toPicture(LocalDate dates, List<String> urls){
        return CloudResponseDTO.PictureDto.builder()
                .date(dates)
                .urlList(urls)
                .build();
    }

}
