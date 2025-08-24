package com.studenthelper.task;

import com.studenthelper.task.DTO.TaskDTO;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
public class TaskSerializationTests {

    @Autowired
    private JacksonTester<TaskDTO> jacksonTester;

    @Autowired
    private JacksonTester<List<TaskDTO>> listJacksonTester;

    String listJson = Files.readString(Paths.get("src/test/resources/list_task.json"));
    String taskJson = Files.readString(Paths.get("src/test/resources/single_task.json"));
    List<TaskDTO> tasks = List.of(
            new TaskDTO(
                    1L,
                    "Project Proposal",
                    "Submit project proposal document",
                    LocalDate.of(2025, 9, 15),
                    LocalTime.of(17, 0),
                    "alice123",
                    "Intro to Programming",
                    "75/100",
                    true
            ),
            new TaskDTO(
                    2L,
                    "Midterm Exam",
                    "Complete midterm exam covering chapters 1-5",
                    LocalDate.of(2025, 10, 10),
                    LocalTime.of(9, 30),
                    "bob456",
                    "Circuit Analysis",
                    "",
                    false
            ),
            new TaskDTO(
                    3L,
                    "Final Presentation",
                    "Prepare slides and present project",
                    LocalDate.of(2025, 12, 5),
                    LocalTime.of(14, 0),
                    "charlie789",
                    "Quantum Mechanics",
                    "",
                    false
            )
    );

    TaskDTO task = tasks.get(0);




    public TaskSerializationTests() throws IOException {
    }

    @Test
    public void serializeTaskTest() throws IOException, JSONException {
        JSONAssert.assertEquals(taskJson, jacksonTester.write(task).getJson(), true);
    }

    @Test
    public void deserializeTaskTest() throws IOException {
        assertThat(jacksonTester.parseObject(taskJson)).isEqualTo(task);
    }

    @Test
    public void serializeListTaskTest() throws IOException, JSONException {
        JSONAssert.assertEquals(listJson, listJacksonTester.write(tasks).getJson(), true);
    }

    @Test
    public void deserializeListTaskTest() throws IOException {
        assertThat(listJacksonTester.parse(listJson)).isEqualTo(tasks);
    }
}
