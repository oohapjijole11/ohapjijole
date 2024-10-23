package com.sparta.final_project.domain.user.entity;

import com.sparta.final_project.config.AuthUser;
import com.sparta.final_project.domain.item.entity.Item;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String password;

    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Item> items;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRating rating;

    // 회원탈퇴 유무
    private Boolean isdeleted = false;

    public User(String email, String password, String name, UserRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    // AuthUser로 User만들 때
    public User(String email, String name, UserRole role) {
        this.email = email;
        this.name = name;
        this.role = role;
    }

    // 유저 생성 AuthUser로 하는방식
    public static User fromAuthUser(AuthUser authUser) {
        return new User(authUser.getName(), authUser.getEmail(), authUser.getRole());
    }

    // 회원 탈퇴 메소드
    public void deletedUser(String email, String password) {
        this.isdeleted = true;
    }


}
