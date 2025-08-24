package com.studenthelper.service;

import com.studenthelper.exceptions.AccessDeniedException;
import com.studenthelper.user.model.User;
import com.studenthelper.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private final UserRepository userRepository;

    SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Unauthorized");
        }
        String username = authentication.getName();

        User user = userRepository.findByUsername(username).get();

        return user.getId();
    }


    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Unauthorized");
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).get();
        return user;
    }
}
