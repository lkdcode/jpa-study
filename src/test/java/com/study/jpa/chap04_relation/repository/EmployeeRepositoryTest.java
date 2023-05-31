package com.study.jpa.chap04_relation.repository;

import com.study.jpa.chap04_relation.entity.Department;
import com.study.jpa.chap04_relation.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class EmployeeRepositoryTest {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @BeforeEach
    void bulkInsert() {

        Department d1 = Department.builder()
                .name("영업부")
                .build();

        Department d2 = Department.builder()
                .name("개발부")
                .build();

        departmentRepository.save(d1);
        departmentRepository.save(d2);

        for (int i = 1; i <= 4; i++) {
            if (i % 2 == 0) {
                employeeRepository.save(Employee.builder()
                        .name("이름" + i)
                        .department(d1)
                        .build()
                );
                continue;
            }
            employeeRepository.save(Employee.builder()
                    .name("이름" + i)
                    .department(d2)
                    .build()
            );
        }

    }

    @Test
    @DisplayName("특정 사원의 정보 조회")
//    @Transactional
//    @Rollback(false)
    void findOneTest() {
        //given
        Long id = 2L;

        //when
        Employee employee = employeeRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException("사원이 없음")
                );

        //then
        System.out.println("\n\n\n");
        System.out.println("employee = " + employee);
        System.out.println("\n\n\n");

    }

    @Test
    @DisplayName("부서 정보 조회")
    void findDeptTest() {

        //given
        Long id = 1L;

        //when
        Department department = departmentRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("")
                );

        
        //then
        System.out.println("\n\n\n");
        System.out.println("department = " + department);
        System.out.println("\n\n\n");

    }


}