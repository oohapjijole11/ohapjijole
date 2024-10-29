package com.sparta.final_project.domain.item.entity;

import com.sparta.final_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Items")
public class Item implements Serializable {
    private static final long serialVersionUID = 1L; // 직렬화 버전 UID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    @ElementCollection
    @CollectionTable(name = "item_images", joinColumns = @JoinColumn(name = "item_id"))
    private List<String> imageUrls;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    // 생성자 추가 (선택 사항)
    public Item(String name, String description, List<String> imageUrls, User user) {
        this.name = name;
        this.description = description;
        this.imageUrls = imageUrls;
        this.user = user;
    }
    public void update(String name, String description, List<String> imageUrls) {
        this.name = name;
        this.description = description;
        this.imageUrls = imageUrls;
    }
}
