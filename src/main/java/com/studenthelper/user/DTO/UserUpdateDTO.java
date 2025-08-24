package com.studenthelper.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateDTO {
    String username;
    int age;
    String email;
    String university;
    String major;
}
