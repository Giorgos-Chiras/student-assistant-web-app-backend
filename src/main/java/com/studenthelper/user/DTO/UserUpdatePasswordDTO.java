package com.studenthelper.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdatePasswordDTO {
    String oldPassword;
    String newPassword;
    String newConfirmPassword;
}
