package com.studenthelper.task.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TaskDTO {
    Long id;
    String name;
    String description;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dueDate;
    private LocalTime dueTime;
    String userName;
    String courseName;
    String grade;
    boolean completed;
}
