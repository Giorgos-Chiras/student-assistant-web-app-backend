package com.studenthelper.professor.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfessorUpdateDTO {
    private String name;
    private String email;
    private String officeNumber;
}
