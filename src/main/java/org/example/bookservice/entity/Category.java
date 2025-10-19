package org.example.bookservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(
            name = "category_seq",
            sequenceName = "CATEGORY_SEQ",
            allocationSize = 1
    )
    private Long id;

    @Column(length = 255, nullable = false, unique = true)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "delete_flg")
    private Boolean deleteFlg = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
