package org.example.bookservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookservice.entity.Admin;
import org.example.bookservice.repository.AdminRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDetailsServiceImpl implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .roles(admin.getRole())
                .build();
    }
}
