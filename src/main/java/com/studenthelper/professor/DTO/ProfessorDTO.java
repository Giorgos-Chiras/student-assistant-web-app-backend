package com.studenthelper.professor.DTO;

import com.studenthelper.course.DTO.CourseProfessorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProfessorDTO {
    private Long id;
    private String name;
    private String email;
    private String officeNumber;
    List<CourseProfessorDTO> courses;
}
