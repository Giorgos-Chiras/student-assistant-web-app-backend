package com.studenthelper.task.mapper;

import com.studenthelper.course.model.Course;
import com.studenthelper.course.repository.CourseRepository;
import com.studenthelper.service.SecurityService;
import com.studenthelper.task.DTO.TaskCreateDTO;
import com.studenthelper.task.DTO.TaskDTO;
import com.studenthelper.task.DTO.TaskUpdateDTO;
import com.studenthelper.task.model.Task;
import com.studenthelper.user.model.User;
import com.studenthelper.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Component
public class TaskMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SecurityService securityService;

    public TaskDTO toDTO(Task task) {
        Long id = task.getId();

        String name = task.getName();
        String description = task.getDescription();
        LocalDate dueDate = task.getDueDate();
        LocalTime dueTime = task.getDueTime();

        String userName = null;
        if (task.getUser() != null) {
            userName = task.getUser().getUsername();
        }

        String courseName = null;
        if (task.getCourse() != null) {
            courseName = task.getCourse().getName();
        }

        String grade = task.getGrade();
        boolean completed = task.getCompleted();


        return new TaskDTO(id, name, description, dueDate, dueTime, userName, courseName,
                grade, completed);
    }

//    public Task toEntity(TaskDTO taskDTO) {
//        Long id = taskDTO.getId();
//
//        String name = taskDTO.getName();
//        String description = taskDTO.getDescription();
//        LocalDate dueDate = taskDTO.getDueDate();
//        LocalTime dueTime = taskDTO.getDueTime();
//        Optional<User> user = userRepository.findById(taskDTO.getUserId());
//        Optional<Course> course = courseRepository.findById(taskDTO.getCourseId());
//        String grade = taskDTO.getGrade();
//        boolean completed = taskDTO.isCompleted();
//
//
//        return new Task(id, name, description, dueDate, dueTime, user.get(), course.get(),
//                grade, completed);
//    }

    public Task toEntity(TaskCreateDTO taskCreateDTO) {

        Long id = null;
        String name = taskCreateDTO.getName();
        String description = taskCreateDTO.getDescription();
        LocalDate dueDate = taskCreateDTO.getDueDate();
        LocalTime dueTime = taskCreateDTO.getDueTime();

        Optional<User> user = userRepository.findById(securityService.getCurrentUserId());
        Optional<Course> course = courseRepository.findById(taskCreateDTO.getCourseId());

        String grade = "";
        boolean completed = false;

        return new Task(id, name, description, dueDate, dueTime, user.get(), course.get(),grade, completed);
    }

    public  Task toEntity(TaskUpdateDTO taskUpdateDTO) {
        String name = taskUpdateDTO.getName();
        String description = taskUpdateDTO.getDescription();
        LocalDate dueDate = taskUpdateDTO.getDueDate();
        LocalTime dueTime = taskUpdateDTO.getDueTime();

        Optional<User> user = userRepository.findById(securityService.getCurrentUserId());
        Optional<Course> course = courseRepository.findById(taskUpdateDTO.getCourseId());
        String grade = taskUpdateDTO.getGrade();
        Boolean completed = taskUpdateDTO.isCompleted();

        return new Task(null, name, description, dueDate, dueTime, user.get(), course.get(),
                grade, completed);
    }

}
