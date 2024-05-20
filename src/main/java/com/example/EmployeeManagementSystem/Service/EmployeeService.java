package com.example.EmployeeManagementSystem.Service;

import com.example.EmployeeManagementSystem.Model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public interface EmployeeService {
    public Employee saveEmployee(Map<String, Object> params);

    public Employee findByEmpId(Long id);

    public Employee findEmployeeById(Long id);

    public Employee findByEmail(String email);

    public Page<Employee> findByDepartment(String department,Pageable pageable);

    public Page<Employee> findAllEmployees(Pageable pageable);

    public void deleteEmployee(Long id);

    public void deleteAllEmployees();

    public Employee updateEmployee(Long id, String name, String email, String department, String position, Integer salary, Long phone);

    public Page<Employee> findByDepartmentAndPosition(String department, String position, Pageable pageable);

    public Page<Employee> findBySalaryBetween(Pageable pageable, int minSalary, int maxSalary);

    public Employee findByPhoneNumber(Long phone);

    public Employee findEmployeeByPhoneNumber(Long phone);

    public List<Employee> findAll();
}
