package org.example.bookservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "NATIONALITY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nationality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Oracle có thể dùng SEQUENCE
    @Column(name = "NATIONALITY_ID")
    private Long id;

    @Column(name = "CODE", length = 10, nullable = false, unique = true)
    private String code;

    @Column(name = "NAME_VI", length = 100, nullable = false)
    private String nameVi;

    @Column(name = "NAME_EN", length = 100)
    private String nameEn;

    @Column(name = "STATUS", length = 1)
    private String status = "A"; // A=Active, I=Inactive

    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

