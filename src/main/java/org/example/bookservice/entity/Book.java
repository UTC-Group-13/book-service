package org.example.bookservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
    @SequenceGenerator(
            name = "book_seq",
            sequenceName = "BOOK_SEQ",
            allocationSize = 1
    )
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(length = 20, unique = true)
    private String isbn;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @Column(name = "publish_year")
    private Integer publishYear;

    @Column(length = 50)
    private String language;

    private Integer quantity = 1;

    private Integer price = 0;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "delete_flg")
    private Boolean deleteFlg = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
