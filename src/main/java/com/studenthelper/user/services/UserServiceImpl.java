package com.studenthelper.user.services;

import com.studenthelper.exceptions.AccessDeniedException;
import com.studenthelper.service.SecurityService;
import com.studenthelper.user.DTO.UserUpdateEmailDTO;
import com.studenthelper.user.DTO.UserUpdatePasswordDTO;
import com.studenthelper.user.exceptions.InvalidPasswordException;
import com.studenthelper.user.exceptions.UserConflictException;
import com.studenthelper.user.exceptions.UserEmailConflictException;
import com.studenthelper.user.exceptions.UserNotFoundException;
import com.studenthelper.user.model.User;
import com.studenthelper.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final SecurityService securityService;
    private final PasswordEncoder passwordEncoder;

    UserServiceImpl(UserRepository userRepository, SecurityService securityService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.securityService = securityService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User addUser(User user) {
        if (userRepository.existsByUsername(user.getUsername()) || userRepository.existsByEmail(user.getEmail())) {
            throw new UserConflictException();
        }
        return userRepository.save(user);

    }

    @Override
    public User getUserById(Long id) {
        Optional<User> user = Optional.empty();
        if(Objects.equals(id, securityService.getCurrentUserId())) {
            user = userRepository.findById(id);
        }
        else{
            throw new AccessDeniedException("Can't access this user");
        }


        return user.orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User getUserByName(String username){
        return userRepository.findByUsernameAndId(username,securityService.getCurrentUserId()).orElseThrow(UserNotFoundException::new);
    }


    @Override
    public List<User> getAllUsers() {
        Optional<List<User>> userOptional = Optional.of(userRepository.
                findAllById(securityService.getCurrentUserId())).get();
        return userOptional.orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void updateUser(Long id, User newUser) {

        User user = getUserById(id);

        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        user.setAge(newUser.getAge());
        user.setUniversity(newUser.getUniversity());
        user.setMajor(newUser.getMajor());

        userRepository.save(user);
    }

    @Override
    public void updateUserPassword(Long id, UserUpdatePasswordDTO newPassword) {
        User user = getUserById(id);
        if(!validatePassword(newPassword.getNewPassword(), newPassword.getNewConfirmPassword())){
            throw new InvalidPasswordException();
        }
        String oldHashedPassword = passwordEncoder.encode(newPassword.getOldPassword());
        String hashedPassword = passwordEncoder.encode(newPassword.getNewPassword());

        if(passwordEncoder.matches(newPassword.getOldPassword(), oldHashedPassword)){
            user.setPassword(hashedPassword);
            userRepository.save(user);
        }
        else{
            throw new InvalidPasswordException();
        }
    }

    @Override
    public void updateUserEmail(Long id, UserUpdateEmailDTO newEmail){
        User user = getUserById(id);
        if(newEmail.getNewEmail().equals(user.getEmail())){
            throw new UserEmailConflictException();
        }
        else {
            user.setEmail(newEmail.getNewEmail());
            userRepository.save(user);
        }
    }

    @Override
    public void deleteUser(Long id){
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    public boolean validatePassword(String password, String confirmPassword) {
        if(password == null || confirmPassword == null) {
            return false;
        }

        boolean passwordsMatch = password.equals(confirmPassword);
        boolean isEmpty = password.isEmpty();
        boolean validLength = password.length() >= 8;
        boolean containsLowercase = password.matches(".*[a-z].*");
        boolean containsUppercase = password.matches(".*[A-Z].*");
        boolean containsDigit = password.matches(".*[0-9].*");
        boolean containsSpecialCharacter = password.matches(".*[@$!%*?&].*");

        return passwordsMatch && !isEmpty && validLength
                && containsLowercase && containsUppercase
                && containsDigit && containsSpecialCharacter;
    }


}
