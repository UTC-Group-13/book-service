package org.example.bookservice.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface AdminDetailsService {
    public UserDetails loadUserByUsername(String username);
}
