package UMC.WithYou.infra.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3PreSignServiceImpl implements S3PreSignService {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private final S3Presigner s3Presigner;

  private final S3Client s3Client;

  @Override
  public String generatePresignedUrl(String idString, S3FileType fileType) {
    String s3Key = generateS3Key(idString, fileType);
    
    // presigned URL 생성 (유효 시간: 5분)
    PutObjectRequest objectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(s3Key)
        .build();
    
    PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(5))
        .putObjectRequest(objectRequest)
        .build();
    
    PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
    
    return presignedRequest.url().toString();
  }

  @Override
  public String generateDownloadUrl(String idString, S3FileType fileType) {
    String s3Key = generateS3Key(idString, fileType);

    GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(s3Key).build();
    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(5))
        .getObjectRequest(getObjectRequest)
        .build();
    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
    
    return presignedRequest.url().toString();
  }

  @Override
  public Boolean deleteFile(String idString, S3FileType fileType) {
    String s3Key = generateS3Key(idString, fileType);
    
    DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucket).key(s3Key).build();
    DeleteObjectResponse response = s3Client.deleteObject(deleteObjectRequest);

    return response.sdkHttpResponse().isSuccessful();
  }

  private String generateS3Key(String idString, S3FileType fileType) {
    // 파일 타입에 따른 경로 설정
    String path = switch (fileType) {
      case PROFILE -> "users/" + idString + "/profile.png";
      case LOG_BANNER -> "travels/" + idString + "/log_banner.png";
      case POD_BANNER -> "travels/" + idString + "/pod_banner.png";
      case POST -> "travels/" + idString + "/posts.png";
      case MEDIA -> "travels/" + idString + "/media.png";
    };
    return path;
  }

}