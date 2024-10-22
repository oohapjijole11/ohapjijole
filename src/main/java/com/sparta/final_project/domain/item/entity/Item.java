package com.sparta.final_project.domain.item.entity;

import com.sparta.final_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(nullable = false, length = 300)
    private String itemName;

    @Column(length = 500)
    private String itemDescription;

    @Column(length = 500)
    private String itemUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 외래 키 명시
    private User user;

    // 생성자
    public Item(String itemName, String itemDescription, String itemUrl, User user) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemUrl = itemUrl;
        this.user = user;
    }
}
