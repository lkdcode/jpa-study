package com.study.jpa.chap04_relation.repository;

import com.study.jpa.chap04_relation.entity.Department;
import com.study.jpa.chap04_relation.entity.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(false)
//@WebAppConfiguration
class DepartmentRepositoryTest {

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    EmployeeRepository employeeRepository;


    @Test
    @DisplayName("특정 부서를 조회하면 해당 부서원들도 함께 조회되어야 한다.")
    void findDeptTest() {
        //given
        Long id = 2L;

        //when
        Department department = departmentRepository
                .findById(id)
                .orElseThrow();

        //then
        System.out.println("\n\n\n");
        System.out.println("department = " + department);
        System.out.println("department = " + department.getEmployees());
        System.out.println("\n\n\n");

    }


    @Test
    @DisplayName("Lazy 로딩과 Eager 로딩의 차이")
    void LazyAndEagerTest() {
        // 3번 사원을 조회하고 싶은데 굳이 부서정보는 필요 없다.
        // Join 이 필요하지 않다.
        //given
        Long id = 3L;

        //when
        Employee employee = employeeRepository.findById(id)
                .orElseThrow();

        //then
        System.out.println("\n\n\n");
//        System.out.println("employee = " + employee);
        System.out.println("employee.getDepartment() = " + employee.getDepartment());
        System.out.println("\n\n\n");

    }


    @Test
    @DisplayName("양방향 연관관계에서 연관데이터의 수정")
    void changeDeptTest() {
        // 3번 사원의 부서를 2번부서에서 1번부서로 변경해야 한다.
        // given
        Employee foundEmp = employeeRepository.findById(3L).orElseThrow();
        Department newDept = departmentRepository.findById(1L).orElseThrow();


        // 사원쪽에서 부서만 변경하는게 아니라
        foundEmp.setDepartment(newDept);
        employeeRepository.save(foundEmp);


//        newDept.getEmployees().add(foundEmp);


        // when
        // 1번 부서 정보를 조회해서 모든 사원을 보겠다.
        Department foundDept = departmentRepository.findById(2L).orElseThrow();


        // then

        System.out.println("\n\n\n");
        foundDept.getEmployees().forEach(System.out::println);
        System.out.println("\n\n\n");


    }


    @Test
    @DisplayName("N+1 문제 발생 예시")
    void NPlusExTest() {
        //given
        List<Department> departments = departmentRepository.findAll();

        //when

        departments.forEach(dept -> {
            System.out.println("\n\n ===== 사원 리스트 ===== \n\n");

            List<Employee> employees = dept.getEmployees();
            System.out.println(employees);

            System.out.println("\n\n");
        });

        //then
    }
}