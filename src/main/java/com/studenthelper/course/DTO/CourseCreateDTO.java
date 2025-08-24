package com.studenthelper.course.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCreateDTO {
    private String courseId;
    private String name;
    private int ects;
    private Long professorId;
}
