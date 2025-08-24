package com.studenthelper.user.services;

import com.studenthelper.user.DTO.UserUpdateEmailDTO;
import com.studenthelper.user.DTO.UserUpdatePasswordDTO;
import com.studenthelper.user.model.User;

import java.util.List;

public interface UserService{

    User addUser(User user);

    User getUserById(Long id);

    User getUserByName(String username);

    List<User> getAllUsers();

    void updateUser(Long id, User user);

    void updateUserPassword(Long id, UserUpdatePasswordDTO newPassword);

    void updateUserEmail(Long id, UserUpdateEmailDTO newEmail);


    void deleteUser(Long id);

    boolean validatePassword(String password, String confirmPassword);
}
