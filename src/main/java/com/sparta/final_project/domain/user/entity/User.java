package com.sparta.final_project.domain.user.entity;

import com.sparta.final_project.domain.item.entity.Item;
import com.sparta.final_project.domain.ticket.entity.BuyTickets;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Setter
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;


    @Enumerated(EnumType.STRING)
    @Column
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Item> items;

    @Enumerated(EnumType.STRING)
    @Column
    private UserRating rating;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BuyTickets> tickets;

    // 회원탈퇴 유무
    private Boolean isdeleted = false;

    @Column(name="user_slack_url", nullable = false, length = 100)
    private String slackUrl;

    public User(String email, String password, String name, UserRole role, String slackUrl) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.slackUrl = slackUrl;
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
//    public static User fromAuthUser(AuthUser authUser) {
//        List<String> roles = authUser.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .toList();
//        // Handle multiple roles or pick the main role
//        UserRole role = UserRole.of(roles.get(0)); // Assuming single role for now
//        return new User(authUser.getId(), authUser.getEmail(), role);
//    }

    // 회원 탈퇴 메소드
    public void deletedUser(String email, String password) {
        this.isdeleted = true;
    }


    public void updateUserRole(UserRole newUserRole){
        this.role = newUserRole;
    }
}
