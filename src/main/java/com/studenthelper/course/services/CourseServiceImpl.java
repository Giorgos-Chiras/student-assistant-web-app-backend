package com.studenthelper.course.services;

import com.studenthelper.course.exception.CourseNotFoundException;
import com.studenthelper.course.model.Course;
import com.studenthelper.course.repository.CourseRepository;
import com.studenthelper.exceptions.AccessDeniedException;
import com.studenthelper.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final SecurityService securityService;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, SecurityService securityService) {
        this.courseRepository = courseRepository;
        this.securityService = securityService;
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findByIdAndUser(id,securityService.getCurrentUser()).
                orElseThrow(CourseNotFoundException::new);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAllByUser(securityService.getCurrentUser()).get();
    }

    @Override
    public void addCourse(Course course) {
        course.setUser(securityService.getCurrentUser());
        if(!course.getProfessor().getUserId().equals(course.getUser().getId())) {
            throw new AccessDeniedException("Cannot access this professor");
        }
        courseRepository.save(course);
    }


    @Override
    public void updateCourse(Long id, Course updatedCourse) {
        Course course = getCourseById(id);
        course.setName(updatedCourse.getName());
        course.setCourseId(updatedCourse.getCourseId());
        course.setEcts(updatedCourse.getEcts());
        course.setProfessor(updatedCourse.getProfessor());

        courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long id) {
        //Check if it exists, throws exception if it doesn't
        Course course = getCourseById(id);
        courseRepository.deleteById(id);

    }


}
