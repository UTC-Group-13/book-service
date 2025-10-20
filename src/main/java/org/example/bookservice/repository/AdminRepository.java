package org.example.bookservice.repository;

import org.example.bookservice.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findAdminsById(Integer id);
    Optional<Admin> findByUsername(String username);
}
