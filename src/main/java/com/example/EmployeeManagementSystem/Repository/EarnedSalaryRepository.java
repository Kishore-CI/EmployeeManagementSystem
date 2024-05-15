package com.example.EmployeeManagementSystem.Repository;

import com.example.EmployeeManagementSystem.Model.EarnedSalary;
import com.example.EmployeeManagementSystem.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Month;
import java.util.List;

public interface EarnedSalaryRepository extends JpaRepository<EarnedSalary,Long> {

    public List<EarnedSalary> findByemployee(Employee employee);

    public EarnedSalary findByEmployeeAndMonth(Employee employee, Month month);
}
