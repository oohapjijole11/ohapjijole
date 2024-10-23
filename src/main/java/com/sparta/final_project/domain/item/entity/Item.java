package com.sparta.final_project.domain.item.entity;

import com.sparta.final_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long itemId;

    @Column(nullable = false, length = 300)
    private String itemName;

    @Column(length = 500)
    private String itemDescription;

    // List로 변경
    @ElementCollection
    private List<String> itemUrls;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    // 생성자
    public Item(String itemName, String itemDescription, List<String> itemUrls, User user) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemUrls = itemUrls;
        this.user = user;
    }

//    public Long getUserId() {
//        return user != null ? user.getId() : null; // null 체크
//    }

}
