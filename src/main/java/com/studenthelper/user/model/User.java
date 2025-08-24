package com.studenthelper.user.model;

import com.studenthelper.course.model.Course;
import com.studenthelper.task.model.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Table(name = "users")
@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String username;
    String password;
    int age;
    String email;
    String university;
    String major;

    @OneToMany(mappedBy = "user")
    List<Course> courses;

    @OneToMany(mappedBy = "user")
    List<Task> tasks ;
}
