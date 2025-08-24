package com.studenthelper.task.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TaskUpdateDTO {
    String name;
    String description;

    private LocalDate dueDate;
    private LocalTime dueTime;
    Long courseId;
    String grade;
    boolean completed;
}

