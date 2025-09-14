package org.example.bookservice.repository;

import org.example.bookservice.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, Integer> {
    List<Email> findAllByStatus(Integer status);
}