package UMC.WithYou.feature.cloud.service;

import UMC.WithYou.feature.cloud.controller.CloudRequestDTO;
import UMC.WithYou.feature.cloud.controller.CloudResponseDTO;
import UMC.WithYou.feature.cloud.domain.Cloud;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudService {
    Cloud createCloud(CloudRequestDTO.CloudJoinDto request, List<MultipartFile> files);
    List<CloudResponseDTO.PictureDto> getPictures(Long travelLog);
    Cloud deletePictures(Long cloudId, List<String> files);
}
