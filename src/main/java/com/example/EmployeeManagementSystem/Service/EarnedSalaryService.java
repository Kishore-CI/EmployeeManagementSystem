package com.example.EmployeeManagementSystem.Service;

import com.example.EmployeeManagementSystem.Model.Employee;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Optional;


public interface EarnedSalaryService {
    public Double calculateEarnedSalary(Employee employee, LocalDate startdate, LocalDate endDate);

    public Double getEarnedSalary(Long id, boolean recalculate, Optional<Month> month, Optional<Year> year, Optional<LocalDate> startDate, Optional<LocalDate> endDate);

    public void deleteEarnedSalary(Long id,Month month);

    public void deleteAllEarnedSalary();
}