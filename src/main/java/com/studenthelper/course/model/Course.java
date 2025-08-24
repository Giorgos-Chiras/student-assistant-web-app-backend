package com.studenthelper.course.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.studenthelper.professor.model.Professor;
import com.studenthelper.task.model.Task;
import com.studenthelper.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"professor", "user"})
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseId;
    private String name;
    private int ects;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    Professor professor;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    List<Task> tasks;
}
