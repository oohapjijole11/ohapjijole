package com.sparta.final_project.domain.item.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "item_attachments")
public class ItemAttachments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_attachment_id")
    private Long id;

    @Column(nullable = false)
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false) // 외래 키 명시
    private Item item; // Item 엔티티와의 관계 설정

    public ItemAttachments(String fileUrl, Item item) {
        this.fileUrl = fileUrl;
        this.item = item;
    }
}