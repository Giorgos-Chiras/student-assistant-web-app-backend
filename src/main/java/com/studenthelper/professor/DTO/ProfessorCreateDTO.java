package com.studenthelper.professor.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfessorCreateDTO {
    private String name;
    private String email;
    private String officeNumber;
}
