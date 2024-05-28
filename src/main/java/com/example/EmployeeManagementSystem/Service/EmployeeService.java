package com.example.EmployeeManagementSystem.Service;

import com.example.EmployeeManagementSystem.Model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public interface EmployeeService {
    /**
     * Saves an employee to the database. Validates for all parameters individually
     * @param params Map
     * @return Employee
     */
    public Employee saveEmployee(Map<String, Object> params);

    /**
     * Utility method to find an employee by id. Used by other methods in this service and methods from other service
     * @param id
     * @return Employee
     */
    public Employee findByEmpId(Long id);

    /**
     * Finds an employee by Id. Used by Employee controller
     * @param id
     * @return Email
     */
    public Employee findEmployeeById(Long id);

    /**
     * Finds an employee by email with validation.
     * @param email
     * @return Employeee
     */
    public Employee findByEmail(String email);

    /**
     * Finds all the employees by their department
     * @param department
     * @param pageable
     * @return Page<Employee>
     */
    public Page<Employee> findByDepartment(String department,Pageable pageable);

    /**
     * Finds all the employees in the database. Used by Employee controller.
     * @param pageable
     * @return Page<Employee>
     */
    public Page<Employee> findAllEmployees(Pageable pageable);

    /**
     * Finds and deletes and employee by id.
     * @param id
     */
    public void deleteEmployee(Long id);

    /**
     * Finds and deletes all employees on database
     */
    public void deleteAllEmployees();

    /**
     * Updates an employee by the provided parameters. Validates for duplicates with email and phone number.
     * @param id
     * @param name
     * @param email
     * @param department
     * @param position
     * @param salary
     * @param phone
     * @return Employee
     */
    public Employee updateEmployee(Long id, String name, String email, String department, String position, Integer salary, Long phone);

    /**
     * Finds employees in the same department holding the same position.
     * @param department
     * @param position
     * @param pageable
     * @return Page<Employee>
     */
    public Page<Employee> findByDepartmentAndPosition(String department, String position, Pageable pageable);

    /**
     * Finds employees between a salary range
     * @param pageable
     * @param minSalary
     * @param maxSalary
     * @return Page<Employee>
     */
    public Page<Employee> findBySalaryBetween(Pageable pageable, int minSalary, int maxSalary);

    /**
     * Utility method to find an employee by their phone number. Used by other methods in this service and methods from other service
     * @param phone
     * @return Employee
     */
    public Employee findByPhoneNumber(Long phone);

    /**
     * Finds an employee by phone number. Used by employee controller.
     * @param phone
     * @return Employee
     */
    public Employee findEmployeeByPhoneNumber(Long phone);

    /**
     * Utility method used to find all the employees in database. Used by other methods in this service and methods from other service
     * @return
     */
    public List<Employee> findAll();
}
