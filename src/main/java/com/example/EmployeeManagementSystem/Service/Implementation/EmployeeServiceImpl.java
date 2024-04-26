package com.example.EmployeeManagementSystem.Service.Implementation;

import com.example.EmployeeManagementSystem.Model.Employee;
import com.example.EmployeeManagementSystem.Repository.EmployeeRepository;
import com.example.EmployeeManagementSystem.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;


    @Override
    public Employee saveEmployee(String name, String email, String department, String position, int salary) {
//        Creates new employee object from params
        Employee new_employee = new Employee(name, email, department, position, salary);

//        Saves the employee object and returns a reference to it
        return employeeRepository.save(new_employee);
    }
}
