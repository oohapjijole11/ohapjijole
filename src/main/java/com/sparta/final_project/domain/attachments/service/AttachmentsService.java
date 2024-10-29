package com.sparta.final_project.domain.attachments.service;

import com.sparta.final_project.domain.common.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachmentsService {
    private final S3Service s3Service;

    // 단일 및 여러 파일 업로드를 처리하는 통합 메서드
    public List<String> uploadImages(List<MultipartFile> files) {
        // 파일이 비어있거나 null일 때 예외 처리
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        // 단일 파일이라면 리스트로 감싸서 처리
        if (files.size() == 1) {
            return Collections.singletonList(s3Service.uploadFile(files.get(0)));
        }

        // 여러 파일 업로드 시 URL 리스트 반환
        return files.stream()
                .map(s3Service::uploadFile)
                .collect(Collectors.toList());
    }

    // 파일 삭제
    public void deleteImage(String fileUrl) {
        s3Service.deleteImage(fileUrl);
    }
}