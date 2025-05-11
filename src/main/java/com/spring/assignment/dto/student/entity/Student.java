package com.spring.assignment.dto.student.entity;

import com.spring.assignment.dto.department.entity.Department;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private String studentNumber;

    @ManyToOne
    private Department department;
}