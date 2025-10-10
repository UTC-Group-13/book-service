package org.example.bookservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "book_category",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"book_id", "category_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_category_seq")
    @SequenceGenerator(
            name = "book_category_seq",
            sequenceName = "BOOK_CATEGORY_SEQ",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;
}
