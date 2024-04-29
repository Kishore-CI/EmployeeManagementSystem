package com.example.EmployeeManagementSystem.Service;

import com.example.EmployeeManagementSystem.Model.Employee;

import java.util.Map;
import java.util.Objects;

public interface EmployeeService {
    public Employee saveEmployee(Map<String, Object> params);

    public Employee findByEmpId(Long id);
}
