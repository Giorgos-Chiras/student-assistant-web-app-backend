package com.studenthelper.course.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseUpdateDTO {
    private String courseId;
    private String name;
    private int ects;
    private Long professorId;
}
