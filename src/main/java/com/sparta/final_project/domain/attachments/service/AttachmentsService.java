package com.sparta.final_project.domain.attachments.service;

import com.sparta.final_project.domain.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class AttachmentsService {

    private final S3Service s3Service;

    public String uploadAttachmentLinkToCard(MultipartFile file) {

        // S3에 파일 업로드 후 URL 반환
        return s3Service.uploadFile(file);
    }
}
