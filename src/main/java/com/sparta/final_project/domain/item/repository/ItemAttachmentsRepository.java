package com.sparta.final_project.domain.item.repository;

import com.sparta.final_project.domain.item.entity.Item;
import com.sparta.final_project.domain.item.entity.ItemAttachments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemAttachmentsRepository extends JpaRepository<ItemAttachments, Long> {
    List<ItemAttachments> findByItem_ItemId(Long itemId);
    void deleteByItem(Item item);
}
