package com.sparta.final_project.domain.item.service;

import com.sparta.final_project.domain.common.service.S3Service;
import com.sparta.final_project.domain.item.dto.request.ItemSaveRequest;
import com.sparta.final_project.domain.item.dto.request.ItemUpdateRequest;
import com.sparta.final_project.domain.item.dto.response.ItemSaveResponse;
import com.sparta.final_project.domain.item.dto.response.ItemSimpleResponse;
import com.sparta.final_project.domain.item.dto.response.ItemUpdateResponse;
import com.sparta.final_project.domain.item.entity.Item;
import com.sparta.final_project.domain.item.entity.ItemAttachments;
import com.sparta.final_project.domain.item.repository.ItemAttachmentsRepository;
import com.sparta.final_project.domain.item.repository.ItemRepository;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.reository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemAttachmentsRepository itemAttachmentsRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;

    @Transactional
    //Auth유저로 변경하기
    public ItemSaveResponse createItem(ItemSaveRequest itemSaveRequest, Long userId) {
        // 아이템 생성
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Item item = new Item(itemSaveRequest.getItemName(), itemSaveRequest.getItemDescription(), null, user );
        Item savedItem = itemRepository.save(item);

        // 이미지 URL 처리 및 첨부파일 저장
        List<ItemAttachments> attachments = new ArrayList<>();
        for (MultipartFile file : itemSaveRequest.getItemImages()) {
            String fileUrl = s3Service.uploadFile(file); // S3에 파일 업로드
            attachments.add(new ItemAttachments(fileUrl, savedItem)); // 첨부파일 추가
        }
        // 첨부파일 저장
        itemAttachmentsRepository.saveAll(attachments);
        // 응답 반환
        return new ItemSaveResponse(savedItem);
    }

    //조회
    @Transactional(readOnly = true)
    public ItemSimpleResponse getItem(Long itemId) {
        // 아이템 조회
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("아이템을 찾을 수 없습니다."));

        // 첨부파일 조회
        List<ItemAttachments> attachments = itemAttachmentsRepository.findByItem_ItemId(itemId);

        // 파일 URL 목록 초기화
        List<String> fileUrls = new ArrayList<>();
        for (ItemAttachments attachment : attachments) {
            fileUrls.add(attachment.getFileUrl()); // 각 첨부파일의 URL을 추가
        }

        // 응답 객체 반환
        return new ItemSimpleResponse(item.getItemId(), item.getItemName(), item.getItemDescription(), fileUrls, item.getUser().getUserId());
    }
        @Transactional
    public ItemUpdateResponse updateItem(Long itemId, ItemUpdateRequest itemUpdateRequest, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("아이템을 찾을 수 없습니다."));

        // 기존 정보 수정
        item.setItemName(itemUpdateRequest.getItemName());
        item.setItemDescription(itemUpdateRequest.getItemDescription());


        // 기존 첨부파일 삭제 및 새로운 첨부파일 업데이트
        List<ItemAttachments> existingAttachments = itemAttachmentsRepository.findByItem_ItemId(itemId);
        for (ItemAttachments attachment : existingAttachments) {
            String fileUrl = attachment.getFileUrl(); // URL을 가져옴
            if (fileUrl != null) {
                s3Service.deleteFile(fileUrl); // S3에서 기존 파일 삭제
            }
        }
        itemAttachmentsRepository.deleteAll(existingAttachments); // 기존 첨부파일 삭제

        // 새로운 파일 업로드 및 저장
        List<ItemAttachments> newAttachments = new ArrayList<>();
        for (MultipartFile file : itemUpdateRequest.getItemImages()) {
            String fileUrl = s3Service.uploadFile(file); // S3에 파일 업로드
            newAttachments.add(new ItemAttachments(fileUrl, item)); // 새로운 ItemAttachments 객체 추가
        }
        itemAttachmentsRepository.saveAll(newAttachments); // 새 첨부파일 저장

        // 첨부파일 URL 리스트 반환
        List<String> newFileUrls = newAttachments.stream()
                .map(ItemAttachments::getFileUrl)
                .toList();

        itemRepository.save(item); // 아이템 정보 저장
        return new ItemUpdateResponse(item.getItemId(), item.getItemName(), item.getItemDescription(), newFileUrls, item.getUser().getUserId());
    }

    @Transactional
    public void deleteItem(Long itemId, Long creatorId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("아이템을 찾을 수 없습니다."));

        // 삭제할 수 있는 권한 체크
        if (!item.getUser().getUserId().equals(creatorId)) { // User 엔티티에서 ID를 가져옴
            throw new IllegalArgumentException("이 아이템을 삭제할 권한이 없습니다.");
        }

        // 아이템과 연결된 첨부파일 삭제 및 S3에서 파일 제거
        List<ItemAttachments> attachments = itemAttachmentsRepository.findByItem_ItemId(itemId); // 첨부파일 가져오기
        for (ItemAttachments attachment : attachments) {
            s3Service.deleteFile(attachment.getFileUrl()); // S3에서 파일 삭제
        }

        // 첨부파일 삭제
        itemAttachmentsRepository.deleteByItem(item); // 아이템에 연결된 모든 첨부파일 삭제

        // 아이템 삭제
        itemRepository.delete(item);
    }
}