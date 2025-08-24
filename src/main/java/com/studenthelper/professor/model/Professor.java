package com.studenthelper.professor.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.studenthelper.course.model.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "professor")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String officeNumber;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    @JsonManagedReference
    List<Course> courses;

    private Long userId;


}
