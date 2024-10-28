package com.sparta.final_project.domain.user.entity;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.item.entity.Item;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;


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

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Item> items;

    @Enumerated(EnumType.STRING)
    @Column
    private UserRating rating;

    @OneToOne(fetch = FetchType.LAZY)
    private Vaccount vaccount;

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

    private User(Long id, String email, UserRole role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }
    public static User fromAuthUser(AuthUser authUser) {
        List<String> roles = authUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        // Handle multiple roles or pick the main role
        UserRole role = UserRole.of(roles.get(0)); // Assuming single role for now
        return new User(authUser.getId(), authUser.getEmail(), role);
    }

    // 회원 탈퇴 메소드
    public void deletedUser(String email, String password) {
        this.isdeleted = true;
    }

    public void updateUserRole(UserRole newUserRole){
        this.role = newUserRole;
    }
}
