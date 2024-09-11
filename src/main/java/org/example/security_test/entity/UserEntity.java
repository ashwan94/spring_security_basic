package org.example.security_test.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="account")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)  // username 중복 방지
    private String username;

    private String password;

    private String role;
}
