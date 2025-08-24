package com.studenthelper.course.services;

import com.studenthelper.course.model.Course;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseService {

    Course getCourseById(Long id);
    List<Course> getAllCourses();
    void addCourse(Course course);

    void updateCourse(Long id, Course updatedCourse);

    void deleteCourse(Long id);

}
