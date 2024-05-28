package com.example.EmployeeManagementSystem.Repository;

import com.example.EmployeeManagementSystem.Model.EarnedSalary;
import com.example.EmployeeManagementSystem.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Month;
import java.util.List;

public interface EarnedSalaryRepository extends JpaRepository<EarnedSalary,Long> {

    /**
     * Finds all the earned salary records for an employee
     * @param employee
     * @return
     */
    public List<EarnedSalary> findByemployee(Employee employee);

    /**
     * Finds the earned salary record for an employee for a given month
     * @param employee
     * @param month
     * @return
     */
    public EarnedSalary findByEmployeeAndMonth(Employee employee, Month month);

    /**
     * Deletes all the earned salary records for an employee
     * @param employee
     */
    public void deleteByemployee(Employee employee);

}
