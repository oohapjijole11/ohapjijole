package com.sparta.final_project.domain.attachments.controller;

import com.sparta.final_project.domain.attachments.service.AttachmentsService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AttachmentsController {
    private final AttachmentsService attachmentsService;
    // 이미지 업로드
    @PostMapping("/attachments")
    public ResponseEntity<String> uploadImage(
            @RequestPart("file") MultipartFile file) { // 파일 첨부

        String imageUrls = attachmentsService.uploadImage(file);
        return ResponseEntity.ok(imageUrls);
    }
}
