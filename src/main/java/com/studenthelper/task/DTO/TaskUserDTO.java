package com.studenthelper.task.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskUserDTO {
    Long taskId;
    String taskName;
    String taskCourse;
}
