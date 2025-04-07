package UMC.WithYou.feature.member.service;

import UMC.WithYou.S3Service;
import UMC.WithYou.feature.member.controller.MemberResponse;
import UMC.WithYou.feature.member.controller.NameRequest;
import UMC.WithYou.feature.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final S3Service s3Service;
    public MemberResponse getMember(Member member){
        return MemberResponse.builder()
                .imageUrl(member.getImageUrl())
                .name(member.getName()).build();
    }
    @Transactional
    public void updateImage(Member member, MultipartFile imageFile){
        String imageUrl = s3Service.uploadImg(imageFile);
        member.updateImage(imageUrl);
    }
    @Transactional
    public void updateName(Member member, NameRequest request){
        member.updateName(request.getName());
    }
}
