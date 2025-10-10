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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_author_seq")
    @SequenceGenerator(
            name = "book_author_seq",
            sequenceName = "BOOK_AUTHOR_SEQ",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "author_id", nullable = false)
    private Long authorId;
}

