package com.sparta.final_project.domain.attachments.service;

import com.sparta.final_project.domain.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachmentsService {
    private final S3Service s3Service;

    public String uploadImage(MultipartFile file) {
        // S3에 파일 업로드 후 URL 반환
        return s3Service.uploadFile(file);
    }

    // deleteImage 메서드 추가
    public void deleteImage(String fileUrl) {
        // S3에서 이미지 삭제
        s3Service.deleteImage(fileUrl);
    }
}
