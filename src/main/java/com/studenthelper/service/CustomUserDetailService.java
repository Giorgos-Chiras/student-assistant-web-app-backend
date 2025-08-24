package com.studenthelper.service;

import com.studenthelper.user.exceptions.UserNotFoundException;
import com.studenthelper.user.model.User;
import com.studenthelper.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    CustomUserDetailService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username).
                orElseThrow(UserNotFoundException::new);


        return org.springframework.security.core.userdetails.User.
                withUsername(user.getUsername()).
                password(user.getPassword()).build();
    }


}
