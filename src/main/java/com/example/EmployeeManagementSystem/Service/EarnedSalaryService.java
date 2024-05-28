package com.example.EmployeeManagementSystem.Service;

import com.example.EmployeeManagementSystem.Model.Employee;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Optional;


public interface EarnedSalaryService {
    /**
     * Calculates the salary earned by the employee between the given dates based on their attendance
     * @param employee
     * @param startdate
     * @param endDate
     * @return Calculated earned salary
     */
    public Double calculateEarnedSalary(Employee employee, LocalDate startdate, LocalDate endDate);

    /**
     * Retrieves the earned salary for an employee, calculates it if no record is found. Performs validation on inputs
     * @param id
     * @param recalculate
     * @param month
     * @param year
     * @param startDate
     * @param endDate
     * @return Calculated / retrieved earned salary
     */
    public Double getEarnedSalary(Long id, boolean recalculate, Optional<Month> month, Optional<Year> year, Optional<LocalDate> startDate, Optional<LocalDate> endDate);

    /**
     * Deletes an earned salary record for the given employee on the given month
     * @param id
     * @param month
     */
    public void deleteEarnedSalary(Long id,Month month);

    /**
     * deletes all the earned salaries of all the employees.
     */
    public void deleteAllEarnedSalary();
}