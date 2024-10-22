package com.sparta.final_project.domain.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RequiredArgsConstructor
@Component
public class S3Service {
    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 파일 업로드 메서드
    public String uploadFile(MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            // S3에 파일 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
                    // ACL 설정 제거
            );

            // 업로드된 파일의 URL 반환
            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            throw new QueensTrelloException(ErrorCode.FILE_UPLOAD_ERROR); // 커스텀 예외 처리
        }
    }

    // 파일 삭제 메서드
    public void deleteFile(String fileUrl) {
        try {
            String[] split = fileUrl.split("/"); // URL에서 파일 이름 추출
            String fileName = split[split.length - 1];
            amazonS3Client.deleteObject(bucket, fileName); // S3에서 파일 삭제
        } catch (Exception e) {
            throw new QueensTrelloException(ErrorCode.FILE_DELETE_ERROR); // 커스텀 예외 처리
        }
    }
}

