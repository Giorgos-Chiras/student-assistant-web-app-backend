package com.studenthelper.user;

import com.studenthelper.course.DTO.CourseUserDTO;
import com.studenthelper.task.DTO.TaskUserDTO;
import com.studenthelper.user.DTO.UserDTO;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JsonTest
public class UserSerializationTests {

    @Autowired
    private JacksonTester<UserDTO> jacksonTester;

    @Autowired
    private JacksonTester<List<UserDTO>> listJacksonTester;

    List<UserDTO> users = List.of(
            new UserDTO(
                    1L, "alice123", 20, "alice@example.com", "Harvard University", "Computer Science",
                    List.of(new CourseUserDTO(1L, "CS101", "Intro to Programming")),
                    List.of(new TaskUserDTO(1L, "Project Proposal", "Intro to Programming"))
            ),
            new UserDTO(
                    2L, "bob456", 22, "bob@example.com", "MIT", "Electrical Engineering",
                    List.of(new CourseUserDTO(2L, "EE202", "Circuit Analysis")),
                    List.of(new TaskUserDTO(2L, "Midterm Exam", "Circuit Analysis"))
            ),
            new UserDTO(
                    3L, "charlie789", 21, "charlie@example.com", "Stanford University", "Physics",
                    List.of(new CourseUserDTO(3L, "PH301", "Quantum Mechanics")),
                    List.of(new TaskUserDTO(3L, "Final Presentation", "Quantum Mechanics"))
            ),
            new UserDTO(
                    4L, "dana001", 23, "dana@example.com", "UC Berkeley", "Mathematics",
                    List.of(new CourseUserDTO(4L, "MATH210", "Linear Algebra")),
                    List.of()
            ),
            new UserDTO(
                    5L, "eric007", 19, "eric@example.com", "Oxford University", "Philosophy",
                    List.of(new CourseUserDTO(5L, "PHIL100", "Intro to Philosophy")),
                    List.of()
            )
    );


    String userJson;
    String listJson;

    UserDTO user = users.get(0);

    @BeforeEach
    void setUp() throws IOException {
        listJson = Files.readString(Paths.get("src/test/resources/list_user.json"));
        userJson = Files.readString(Paths.get("src/test/resources/single_user.json"));

    }

    @Test
    public void serializeUser() throws IOException, JSONException {
        JSONAssert.assertEquals(userJson, jacksonTester.write(user).getJson(), true);
    }

    @Test
    public void deserializeUser() throws IOException {
        assertThat(jacksonTester.parseObject(userJson)).isEqualTo(user);

    }

    @Test
    public void userListSerializationTest() throws IOException, JSONException {
        JSONAssert.assertEquals(listJson, listJacksonTester.write(users).getJson(), true);
    }

    @Test
    public void userListDeserializationTest() throws IOException {
        assertThat(listJacksonTester.parseObject(listJson)).isEqualTo(users);
    }

}
