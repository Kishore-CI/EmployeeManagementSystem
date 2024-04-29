package com.example.EmployeeManagementSystem.Service.Implementation;

import com.example.EmployeeManagementSystem.Model.Employee;
import com.example.EmployeeManagementSystem.Repository.EmployeeRepository;
import com.example.EmployeeManagementSystem.Service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Map;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private Logger log = LoggerFactory.getLogger(Employee.class);
    @Autowired
    private EmployeeRepository employeeRepository;


    @Override
    public Employee saveEmployee(Map<String,Object> params){
//        Creates new employee object from params
        Employee new_employee = null;

//        Check if there is id parameter present in params Map
        if(params.containsKey("id") && params.get("id")!= null ) {

            log.info("Id parameter type:" + params.get("id").getClass());

//            Check if the employee already exists
            new_employee = findByEmpId(((Integer)params.get("id")).longValue());

//            If employee exists then log it and return an empty employee
            if (new_employee != null) {
                log.info("saveEmployee -> Employee already exists");
                return new Employee();
            }

        }
//        If employee does not exist, create a new employee with the provided parameters
        new_employee = new Employee(
                ((Integer) params.get("id")).longValue(),
                (String) params.get("name"), (String) params.get("email"),
                (String) params.get("position"), (String) params.get("department"),
                (Integer) params.get("salary")
        );
//        Save the newly created employee using the repository
        Employee saved_employee = employeeRepository.save(new_employee);
//        return the saved employee
        return saved_employee;
    }

    @Override
    public Employee findByEmpId(Long id) {
        Employee employee = employeeRepository.findByid(id);
        return employee;
    }
}
