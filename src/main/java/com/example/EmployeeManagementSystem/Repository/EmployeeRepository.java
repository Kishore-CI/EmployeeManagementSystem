package com.example.EmployeeManagementSystem.Repository;

import com.example.EmployeeManagementSystem.Model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    /**
     * Finds an employee by id
     * @param id
     * @return
     */
    public Employee findByid(Long id);

    /**
     * Finds all the employees and paginates the response based on provided parameters
     * @param pageable
     * @return
     */
    public Page<Employee> findAll(Pageable pageable);

    /**
     * Finds an employee by their email
     * @param email
     * @return
     */
    public Employee findByemail(String email);

    /**
     * Finds all the employees in a department and paginates the response based on provided parameters
     * @param department
     * @param pageable
     * @return
     */
    public Page<Employee> findBydepartment(String department, Pageable pageable);

    /**
     * Finds all the employees in a department holding the same position and paginates the response based on provided parameters.
     * @param department
     * @param position
     * @param pageable
     * @return
     */
    public Page<Employee> findByDepartmentAndPosition(String department, String position, Pageable pageable);

    /**
     * Find all the employees in a given salary range and paginates the response based on provided parameters.
     * @param minSalary
     * @param maxSalary
     * @param pageable
     * @return
     */
    public Page<Employee> findBySalaryBetween(int minSalary, int maxSalary, Pageable pageable);

    /**
     * Finds an employee by their phone number
     * @param phone
     * @return
     */
    public Employee findByphone(Long phone);
}
