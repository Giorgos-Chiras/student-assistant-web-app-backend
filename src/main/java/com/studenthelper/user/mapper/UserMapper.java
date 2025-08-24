package com.studenthelper.user.mapper;

import com.studenthelper.course.DTO.CourseUserDTO;
import com.studenthelper.course.model.Course;
import com.studenthelper.task.DTO.TaskUserDTO;
import com.studenthelper.task.model.Task;
import com.studenthelper.user.DTO.UserCreateDTO;
import com.studenthelper.user.DTO.UserDTO;
import com.studenthelper.user.DTO.UserUpdateDTO;
import com.studenthelper.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserDTO toDTO(User user) {
        Long userId = user.getId();
        String name = user.getUsername();
        int age = user.getAge();
        String email = user.getEmail();
        String university = user.getUniversity();
        if (university == null) {
            university = "No University";
        }
        String major = user.getMajor();
        if (major == null) {
            major = "No Major";
        }

        List<CourseUserDTO> courses = new ArrayList<>();
        for (Course course : user.getCourses()) {
            Long id = course.getId();
            String courseId = course.getCourseId();
            String courseName = course.getName();
            CourseUserDTO courseUserDTO = new CourseUserDTO(id, courseId,courseName);
            courses.add(courseUserDTO);
        }

        List<TaskUserDTO> tasks = new ArrayList<>();
        for (Task task : user.getTasks()) {
            Long taskId = task.getId();
            String taskName = task.getName();
            String taskCourse = task.getCourse().getName();
            TaskUserDTO taskUserDTO = new TaskUserDTO(taskId, taskName, taskCourse);
            tasks.add(taskUserDTO);
        }

        return new UserDTO(userId, name, age, email, university, major, courses, tasks);

    }

   public User toEntity(UserUpdateDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setAge(userDTO.getAge());
        user.setEmail(userDTO.getEmail());
        user.setUniversity(userDTO.getUniversity());
        user.setMajor(userDTO.getMajor());
        return user;
    }

    public User toEntity(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setUsername(userCreateDTO.getUsername());

        String password = passwordEncoder.encode(userCreateDTO.getPassword());
        user.setPassword(password);

        user.setAge(userCreateDTO.getAge());
        user.setEmail(userCreateDTO.getEmail());
        user.setUniversity(userCreateDTO.getUniversity());
        user.setMajor(userCreateDTO.getMajor());
        return user;
    }


}
