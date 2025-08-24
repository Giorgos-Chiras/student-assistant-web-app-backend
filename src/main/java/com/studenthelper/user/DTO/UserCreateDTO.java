package com.studenthelper.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    String username;
    String password;
    String confirmPassword;
    int age;
    String email;
    String university;
    String major;
}
