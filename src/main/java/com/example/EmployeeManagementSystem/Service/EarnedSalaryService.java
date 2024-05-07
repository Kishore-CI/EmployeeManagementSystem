package com.example.EmployeeManagementSystem.Service;

import com.example.EmployeeManagementSystem.Model.Employee;
import org.springframework.stereotype.Service;


public interface EarnedSalaryService {
    public Double calculateEarnedSalary(Employee employee);

    public Double getEarnedSalary(Long id);
}