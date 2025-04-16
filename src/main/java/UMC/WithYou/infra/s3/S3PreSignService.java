package UMC.WithYou.infra.s3;

public interface S3PreSignService {
    String generatePresignedUrl(String idString, S3FileType fileType);
    String generateDownloadUrl(String idString, S3FileType fileType);
    Boolean deleteFile(String idString, S3FileType fileType);
}
