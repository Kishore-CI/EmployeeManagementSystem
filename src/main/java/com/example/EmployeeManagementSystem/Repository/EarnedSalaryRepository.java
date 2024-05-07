package com.example.EmployeeManagementSystem.Repository;

import com.example.EmployeeManagementSystem.Model.EarnedSalary;
import com.example.EmployeeManagementSystem.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EarnedSalaryRepository extends JpaRepository<EarnedSalary,Long> {

    public EarnedSalary findByemployee(Employee employee);
}
