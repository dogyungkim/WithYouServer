package UMC.WithYou.feature.cloud.service;

import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.apiPayload.exception.handler.CommonErrorHandler;
import UMC.WithYou.feature.cloud.controller.CloudRequestDTO;
import UMC.WithYou.feature.cloud.controller.CloudResponseDTO;
import UMC.WithYou.feature.cloud.converter.CloudConverter;
import UMC.WithYou.feature.cloud.domain.Cloud;
import UMC.WithYou.feature.cloud.domain.CloudMedia;
import UMC.WithYou.feature.cloud.repository.CloudCustomRepository;
import UMC.WithYou.feature.cloud.repository.CloudMediaRepository;
import UMC.WithYou.feature.cloud.repository.CloudRepository;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.repository.TravelRepository;
import UMC.WithYou.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CloudServiceImpl implements CloudService{
    private final CloudRepository cloudRepository;
    private final TravelRepository travelRepository;
    private final CloudMediaRepository cloudMediaRepository;
    private final CloudCustomRepository cloudCustomRepository;
    private final S3Service s3Service;

    @Override
    public Cloud createCloud(CloudRequestDTO.CloudJoinDto request, List<MultipartFile> files){
        Travel travel = travelRepository.findById(request.getTravelId())
                .orElseThrow(()->new CommonErrorHandler(ErrorStatus.TRAVEL_LOG_NOT_FOUND));

        Cloud cloud = cloudRepository.findByTravel(travel)
                .orElseGet(() -> {
                    Cloud newCloud = CloudConverter.toChangeCloud(travel);
                    return cloudRepository.save(newCloud);
                });

        //String picture=s3Service.uploadImg(files);
        List<String> pictureList=files.stream()
                .map(picture->{
                    return s3Service.uploadImg(picture);
                }).collect(Collectors.toList());

        for (String picture:pictureList){
            CloudMedia cloudMedia=CloudConverter.toMedia(request,cloud,picture);
            cloudMediaRepository.save(cloudMedia);
        }

//        CloudMedia cloudMedia=CloudConverter.toMedia(request,cloud,picture);
//        List<CloudMedia> medias=cloudMediaRepository.findAllByCloud(cloud);
//        for (int i=0;i<medias.size();i++){
//            if (request.getDate().equals(medias.get(i).getDate())) {
//                cloudMedia=medias.get(i);
//                cloudMedia.addUrl(picture);
//                break;
//            }
//        }
//        cloudMediaRepository.save(cloudMedia);

        return cloud;
    }


    @Override
    public List<CloudResponseDTO.PictureDto> getPictures(Long travelLog){
        List<CloudResponseDTO.PictureDto> pictureList=new ArrayList<>();

        Travel travel = travelRepository.findById(travelLog)
                .orElseThrow(()->new CommonErrorHandler(ErrorStatus.TRAVEL_LOG_NOT_FOUND));

        Optional<Cloud> cloud=cloudRepository.findByTravel(travel);
        if (cloud.isEmpty()) return pictureList;

//        Cloud cloud1 = cloudCustomRepository.findByTravelFetchJoinCloud(cloud.get().getId());
        List<CloudMedia> cloudMedia=cloudMediaRepository.findAllByCloud(cloud.get());
        LocalDate start=travel.getStartDate();

        while (!start.isAfter(travel.getEndDate())) {
            List<String> urls=new ArrayList<>();
            for (CloudMedia cloudMedia1:cloudMedia){
                if (cloudMedia1.getDate().isEqual(start)) {
                    urls.add(cloudMedia1.getUrl());
                }
            }
            CloudResponseDTO.PictureDto newPicture=CloudConverter.toPicture(start,urls);
            pictureList.add(newPicture);
            start= start.plus(1, ChronoUnit.DAYS);
        }
//        pictureList=cloud1.getPictureDate().stream()
//                .map(picture->{
//                    return CloudConverter.toPicture(picture);
//                }).collect(Collectors.toList());
        return pictureList;
    }

    @Override
    public Cloud deletePictures(Long cloudId, List<String> files){
        Cloud cloud=cloudRepository.findById(cloudId).get();
        List<CloudMedia> cloudMedias=cloudMediaRepository.findAllByCloud(cloud);

        for(int i=0;i<files.size();i++) {
            for(CloudMedia cloudMedia:cloudMedias){
                if (cloudMedia.getUrl().equals(files.get(i)))
                    cloudMediaRepository.delete(cloudMedia);
            }
        }

        return cloud;
    }
}
