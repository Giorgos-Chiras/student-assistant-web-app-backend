package com.studenthelper.course.controller;

import com.studenthelper.course.DTO.CourseCreateDTO;
import com.studenthelper.course.DTO.CourseDTO;
import com.studenthelper.course.DTO.CourseUpdateDTO;
import com.studenthelper.course.exception.CourseConflictException;
import com.studenthelper.course.exception.CourseNotFoundException;
import com.studenthelper.course.mapper.CourseMapper;
import com.studenthelper.course.model.Course;
import com.studenthelper.course.services.CourseServiceImpl;
import com.studenthelper.exceptions.AccessDeniedException;
import com.studenthelper.professor.services.ProfessorServiceImpl;
import com.studenthelper.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    CourseServiceImpl courseService;

    @Autowired
    ProfessorServiceImpl professorService;
    @Autowired
    private UserService userService;

    @Autowired
    CourseMapper courseMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        CourseDTO courseDTO = courseMapper.toDTO(course);
        return ResponseEntity.ok(courseDTO);
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getCourses() {
        List<Course> courses = courseService.getAllCourses();
        List<CourseDTO> courseDTOList = new ArrayList<>();
        for (Course course : courses) {
            courseDTOList.add(courseMapper.toDTO(course));
        }
        return ResponseEntity.ok(courseDTOList);
    }

    @PostMapping
    public ResponseEntity<Void> addCourse(@RequestBody CourseCreateDTO courseCreateDTO) {
       Course course = courseMapper.toEntity(courseCreateDTO);


        courseService.addCourse(course);
        Long id = course.getId();
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(id).
                toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCourse(@PathVariable Long id, @RequestBody CourseUpdateDTO courseUpdateDTO) {
        Course course = courseMapper.toEntity(courseUpdateDTO);
        courseService.updateCourse(id, course);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @ControllerAdvice
    public static class CourseExceptionHandler {
        @ExceptionHandler(CourseNotFoundException.class)
        public ResponseEntity<Void> handleCourseNotFound(CourseNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        @ExceptionHandler(CourseConflictException.class)
        public ResponseEntity<Void> handleCourseConflict(CourseConflictException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<Void> handleAccessDenied(AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
