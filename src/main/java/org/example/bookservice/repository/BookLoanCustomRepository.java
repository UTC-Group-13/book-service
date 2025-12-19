package org.example.bookservice.repository;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.example.bookservice.entity.BookLoan;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BookLoanCustomRepository {

    @PersistenceContext
    private EntityManager em;

    public Page<BookLoan> search(
            Integer studentId,
            Integer bookId,
            Integer adminId,
            String status,
            LocalDate borrowFrom,
            LocalDate borrowTo,
            LocalDate dueFrom,
            LocalDate dueTo,
            Boolean onlyNotReturned,
            Boolean onlyOverdue,
            String search,
            Pageable pageable
    ) {

        StringBuilder jpql = new StringBuilder("""
            SELECT bl FROM BookLoan bl
            JOIN Book b ON b.id = bl.bookId
            JOIN Student s ON s.id = bl.studentId
            WHERE bl.deleteFlg = false
        """);

        Map<String, Object> params = new HashMap<>();

        // ==== Dynamic conditions ====

        if (studentId != null) {
            jpql.append(" AND bl.studentId = :studentId");
            params.put("studentId", studentId);
        }

        if (bookId != null) {
            jpql.append(" AND bl.bookId = :bookId");
            params.put("bookId", bookId);
        }

        if (status != null) {
            jpql.append(" AND bl.status = :status");
            params.put("status", status);
        }

        if (borrowFrom != null) {
            jpql.append(" AND bl.borrowDate >= :borrowFrom");
            params.put("borrowFrom", borrowFrom);
        }

        if (borrowTo != null) {
            jpql.append(" AND bl.borrowDate <= :borrowTo");
            params.put("borrowTo", borrowTo);
        }

        if (dueFrom != null) {
            jpql.append(" AND bl.dueDate >= :dueFrom");
            params.put("dueFrom", dueFrom);
        }

        if (dueTo != null) {
            jpql.append(" AND bl.dueDate <= :dueTo");
            params.put("dueTo", dueTo);
        }

        if (Boolean.TRUE.equals(onlyNotReturned)) {
            jpql.append(" AND bl.returnDate IS NULL");
        }

        if (Boolean.TRUE.equals(onlyOverdue)) {
            jpql.append("""
                AND bl.returnDate IS NULL
                AND bl.dueDate < CURRENT_DATE
            """);
        }

        if (search != null && !search.trim().isEmpty()) {
            jpql.append("""
                AND (
                    LOWER(s.fullName) LIKE :search
                    OR LOWER(b.title) LIKE :search
                )
            """);
            params.put("search", "%" + search.toLowerCase() + "%");
        }

        jpql.append(" ORDER BY bl.id DESC");

        // ==== Create query ====
        TypedQuery<BookLoan> query = em.createQuery(jpql.toString(), BookLoan.class);
        params.forEach(query::setParameter);

        // ==== Paging ====
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<BookLoan> result = query.getResultList();

        // ==== Count query ====
        String countJpql = jpql.toString()
                .replaceFirst("SELECT bl FROM", "SELECT COUNT(bl) FROM")
                .replaceFirst("ORDER BY bl.id DESC", "");

        TypedQuery<Long> countQuery = em.createQuery(countJpql, Long.class);
        params.forEach(countQuery::setParameter);

        long total = countQuery.getSingleResult();

        return new PageImpl<>(result, pageable, total);
    }
}
