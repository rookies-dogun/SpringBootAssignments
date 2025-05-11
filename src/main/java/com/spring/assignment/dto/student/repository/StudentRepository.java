package com.spring.assignment.dto.student.repository;


import com.spring.assignment.dto.department.entity.Department;
import com.spring.assignment.dto.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
    List<Student> findByDepartment(Department department);

    Student findByStudentNumber(String studentNumber);

}
