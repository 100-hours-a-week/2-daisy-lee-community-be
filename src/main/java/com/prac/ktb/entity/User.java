package com.prac.ktb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column
    private String profileImagePath;

    @Column
    private Date droppedAt;

    @Builder
    public User(String email, String password, String nickname, String profileImagePath, Date droppedAt) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImagePath = profileImagePath;
        this.droppedAt = droppedAt;
    }

}