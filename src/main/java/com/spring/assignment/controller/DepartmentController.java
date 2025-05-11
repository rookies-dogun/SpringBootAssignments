package com.spring.assignment.controller;

import com.spring.assignment.dto.department.entity.Department;
import com.spring.assignment.dto.department.repository.DepartmentRepository;
import com.spring.assignment.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
//        return departmentRepository.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

    @GetMapping(value = "/{id}")
    public Department getDepartmentById(@PathVariable Long id) {
        return getDepartmentNotFound(id);
    }

    private Department getDepartmentNotFound(Long id) {
        return departmentRepository.findById(id) //Optional<Department>
                .orElseThrow(() -> new BusinessException("Department Not Found", HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        return departmentRepository.save(department);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id,
                                                       @RequestBody Department departmentDetails) {
        return departmentRepository.findById(id)
                .map(department -> {
                    department.setName(departmentDetails.getName());
                    department.setCode(departmentDetails.getCode());
                    Department updatedDepartment = departmentRepository.save(department);
                    return ResponseEntity.ok(updatedDepartment);
                })
                //.orElse(ResponseEntity.notFound().build());
                .orElseThrow(() -> new BusinessException("Department Not Found", HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        return departmentRepository.findById(id)
                .map(department -> {
                    departmentRepository.delete(department);
                    return ResponseEntity.ok().<Void>build();
                })
                //.orElse(ResponseEntity.notFound().build());
                .orElseThrow(() -> new BusinessException("Department Not Found", HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/student-count")
    public ResponseEntity<Map<String, Long>> getStudentCount(@PathVariable Long id) {
        if (!departmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Long count = departmentRepository.countStudentsByDepartmentId(id);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping(value = "/name/{name}")
    public Department getDepartmentByName(@PathVariable String name) {
        return departmentRepository.findByName(name);
    }
}