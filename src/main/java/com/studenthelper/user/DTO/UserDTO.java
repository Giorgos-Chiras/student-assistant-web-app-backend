package com.studenthelper.user.DTO;

import com.studenthelper.course.DTO.CourseUserDTO;
import com.studenthelper.task.DTO.TaskUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDTO {
    Long id;
    String username;
    int age;
    String email;
    String university;
    String major;
    List<CourseUserDTO> courses;
    List<TaskUserDTO> tasks;
}
