package com.studenthelper.course.repository;

import com.studenthelper.course.model.Course;
import com.studenthelper.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, PagingAndSortingRepository<Course, Long> {
    boolean existsById(Long id);
    Optional<Course> findByIdAndUser(Long id, User user);
    Optional<List<Course>> findAllByUser(User user);
}
