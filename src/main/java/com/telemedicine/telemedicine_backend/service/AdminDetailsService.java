package com.telemedicine.telemedicine_backend.service;

import com.telemedicine.telemedicine_backend.entity.Admin;
import com.telemedicine.telemedicine_backend.repository.AdminRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public AdminDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Account not found: " + username));

        String role = admin.getRole() == null || admin.getRole().isBlank() ? "ADMIN" : admin.getRole();

        return User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
            .roles(role)
                .build();
    }
}