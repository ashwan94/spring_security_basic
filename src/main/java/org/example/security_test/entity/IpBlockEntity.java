package org.example.security_test.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ip_block")
@Data
public class IpBlockEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "ip")
    private String ip;

    @Column(name = "access_date")
    private String accessDate;
}