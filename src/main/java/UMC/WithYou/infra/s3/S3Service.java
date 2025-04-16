package UMC.WithYou.infra.s3;

import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.apiPayload.exception.handler.CommonErrorHandler;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;

    public String uploadImg(MultipartFile file) {
        String storageFileName = createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, storageFileName, inputStream, objectMetadata)
                    //.withCannedAcl(CannedAccessControlList.PublicReadWrite));
            );
        } catch (IOException e) {
            throw new CommonErrorHandler(ErrorStatus._PICTURE);
        }
        return amazonS3Client.getUrl(bucket, storageFileName).toString();
        //return storageFileName;
    }


    public String uploadMedia(MultipartFile media, String fileName){
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(media.getSize());
        objectMetadata.setContentType(media.getContentType());

        try (InputStream inputStream = media.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    //.withCannedAcl(CannedAccessControlList.PublicReadWrite));
            );
        } catch (IOException e) {
            throw new CommonErrorHandler(ErrorStatus._PICTURE);
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 파일 이름 생성 로직
    public String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new CommonErrorHandler(ErrorStatus._PICTURE);
        }
    }

    public void deleteFile(String fileName) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    public String getFileUrl(String fileName) {
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
}
