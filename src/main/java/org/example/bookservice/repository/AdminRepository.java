package org.example.bookservice.repository;

import org.example.bookservice.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findAdminsById(Integer id);
}
