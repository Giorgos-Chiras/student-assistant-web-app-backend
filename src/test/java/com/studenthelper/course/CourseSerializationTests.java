package com.studenthelper.course;

import com.studenthelper.course.DTO.CourseDTO;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CourseSerializationTests {

    @Autowired
    private JacksonTester<CourseDTO> jacksonTester;

    @Autowired
    private JacksonTester<List<CourseDTO>> jacksonTesterList;


    List<CourseDTO> courses = List.of(
            new CourseDTO(1L, "CS101", "Intro to Programming", 6, "John Smith"),
            new CourseDTO(2L, "EE202", "Circuit Analysis", 5, "Lisa Williams"),
            new CourseDTO(3L, "PH301", "Quantum Mechanics", 7, "Michael Jordan"),
            new CourseDTO(4L, "MATH210", "Linear Algebra", 5, "Lisa Williams"),
            new CourseDTO(5L, "PHIL100", "Intro to Philosophy", 4, "No Professor")
    );

    CourseDTO course = courses.get(0);

    String listJson = Files.readString(Paths.get("src/test/resources/list_course.json"));
    String courseJson = Files.readString(Paths.get("src/test/resources/single_course.json"));

    public CourseSerializationTests() throws IOException {
    }

    @Test
    public void serializeCourse() throws IOException, JSONException {
        JSONAssert.assertEquals(courseJson, jacksonTester.write(course).getJson(), true);
    }

    @Test
    public void deserializeCourse() throws IOException {
        assertThat(jacksonTester.parseObject(courseJson)).isEqualTo(course);

    }

    @Test
    public void serializeListCourse() throws IOException, JSONException {
        JSONAssert.assertEquals(listJson, jacksonTesterList.write(courses).getJson(), true);
    }

    @Test
    public void deserializeListCourse() throws IOException {
        assertThat(jacksonTesterList.parseObject(listJson)).isEqualTo(courses);
    }

}
