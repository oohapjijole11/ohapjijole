package com.sparta.final_project.domain.attachments.controller;

import com.sparta.final_project.domain.attachments.service.AttachmentsService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AttachmentsController {
    private final AttachmentsService attachmentsService;

    // 이미지 업로드
    @PostMapping("/attachments")
    public ResponseEntity<List<String>> uploadImages(
            @RequestPart("files") List<MultipartFile> files) { // 여러 파일을 받을 수 있도록 List<MultipartFile> 사용

        List<String> imageUrls = attachmentsService.uploadImages(files); // 서비스 메서드에 List 전달
        return ResponseEntity.ok(imageUrls); // 여러 파일 URL 리스트 반환
    }
}

