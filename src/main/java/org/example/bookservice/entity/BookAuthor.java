package org.example.bookservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_author")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "author_id", nullable = false)
    private Long authorId;
}

