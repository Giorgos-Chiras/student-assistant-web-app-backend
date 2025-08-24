package com.studenthelper.course.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseProfessorDTO {
    private Long id;
    private String courseId;
    private String name;
    private int ects;
}
