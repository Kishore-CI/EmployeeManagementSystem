package com.example.EmployeeManagementSystem.Service;

import com.example.EmployeeManagementSystem.Model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Objects;

public interface EmployeeService {
    public Employee saveEmployee(Map<String, Object> params);

    public Employee findByEmpId(Long id);

    public Employee findByEmail(String email);

    public Page<Employee> findByDepartment(String department,Pageable pageable);

    public Page<Employee> findAllEmployees(Pageable pageable);

    public Boolean deleteEmployee(Long id);

    public void deleteAllEmployees();

    public Employee updateEmployee(Long id,Map<String,Object> params);
}
