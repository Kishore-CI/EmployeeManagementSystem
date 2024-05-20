package com.example.EmployeeManagementSystem.Repository;

import com.example.EmployeeManagementSystem.Model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    public Employee findByid(Long id);

    public Page<Employee> findAll(Pageable pageable);

    public Employee findByemail(String email);

    public Page<Employee> findBydepartment(String department, Pageable pageable);

    public Page<Employee> findByDepartmentAndPosition(String department, String position, Pageable pageable);

    public Page<Employee> findBySalaryBetween(int minSalary, int maxSalary, Pageable pageable);

    public Employee findByphone(Long phone);
}
