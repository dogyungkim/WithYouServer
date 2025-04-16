package UMC.WithYou.feature.member.service;

import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.apiPayload.exception.GeneralException;
import UMC.WithYou.feature.member.controller.NameRequest;
import UMC.WithYou.feature.member.domain.Member;    
import UMC.WithYou.infra.s3.S3FileType;
import UMC.WithYou.infra.s3.S3PreSignService;
import UMC.WithYou.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final S3Service s3Service;
    private final S3PreSignService s3PreSignService;
    
    @Transactional
    public void updateImage(Member member, MultipartFile imageFile){
        String imageUrl = s3Service.uploadImg(imageFile);
        member.updateImage(imageUrl);
    }
    
    @Transactional
    public void updateName(Member member, NameRequest request){
        member.updateName(request.getName());
    }

    @Transactional
    public String setMemberInfo(Member member, String name){
        if(name.isEmpty()){
            throw new GeneralException(ErrorStatus._NOT_FOUND);
        }

        member.updateName(name);
        
        String memberId = member.getId().toString();
        String presignedUrl = s3PreSignService.generatePresignedUrl(memberId, S3FileType.PROFILE);
        
        member.updateImage(presignedUrl);

        return presignedUrl;
    }
}
