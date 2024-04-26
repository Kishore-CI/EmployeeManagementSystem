package com.example.EmployeeManagementSystem.Service;

import com.example.EmployeeManagementSystem.Model.Employee;

public interface EmployeeService {
    public Employee saveEmployee(String name, String email, String position,
                                 String department, int salary);
}
