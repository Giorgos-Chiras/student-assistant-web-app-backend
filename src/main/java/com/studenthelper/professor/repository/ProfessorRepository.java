package com.studenthelper.professor.repository;

import com.studenthelper.professor.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    boolean existsById(Long id);
    List<Professor> findByUserId(Long userId);
    Optional<Professor> findAllByIdAndUserId(Long id, Long userId);
}
