package com.studenthelper.course.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseUserDTO {
    Long id;
    String courseId;
    String name;
}
