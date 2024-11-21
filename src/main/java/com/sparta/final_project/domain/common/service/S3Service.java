package com.sparta.final_project.domain.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
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

    public String uploadFile(MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());
            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
                    // ACL 설정 제거
            );

            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            throw new OhapjijoleException(ErrorCode._ATTACHMENT_NOT_FOUND);
        }
    }
    public void deleteImage(String fileUrl) {
        try {
            String[] split = fileUrl.split("/");
            amazonS3Client.deleteObject(bucket, split[split.length - 1]);
        } catch (Exception e) {
            throw new OhapjijoleException(ErrorCode._ATTACHMENT_NOT_FOUND);
        }
    }
}

