package com.example.EmployeeManagementSystem.Service.Implementation;

import com.example.EmployeeManagementSystem.Exception.ApiRequestException;
import com.example.EmployeeManagementSystem.Model.Employee;
import com.example.EmployeeManagementSystem.Repository.EmployeeRepository;
import com.example.EmployeeManagementSystem.Service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
        if(params.containsKey("email") && params.get("email")!= null ) {

            log.info("Email parameter type:" + params.get("email").getClass());

//            Check if the employee already exists
            new_employee = findByEmail((String)params.get("email"));

//            If employee exists then log it and return an empty employee
            if (new_employee != null) {
                log.info("saveEmployee -> Employee with email : {} already exists",params.get("email"));
                throw new ApiRequestException("Employee with email : "+params.get("email") + " already exists", HttpStatus.BAD_REQUEST); // try custome exception
            }
        }
//        If employee does not exist, create a new employee with the provided parameters
        new_employee = new Employee(
                (String) params.get("name"), (String) params.get("email"),
                (String) params.get("department"), (String) params.get("position"),
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
        if(employee == null){
            log.info("findByEmpId -> No employee with the id : {} found",id);
        }
        return employee;
    }

    @Override
    public Employee findEmployeeById(Long id){
        Employee employee = findByEmpId(id);
        if(employee == null){
            throw new ApiRequestException("No employee found for id: "+id,HttpStatus.BAD_REQUEST);
        }
        return employee;
    }

    @Override
    public Employee findByEmail(String email) {
//        Find employee by their email
        Employee employee = employeeRepository.findByemail(email);

        if (employee == null){
            log.info("findByEmail -> No employee with the email : {}",email);
        }

        return employee;
    }

    @Override
    public Page<Employee> findByDepartment(String department, Pageable pageable) {
//        Find all employees in the same department
        Page<Employee> employeePage = employeeRepository.findBydepartment(department,pageable);

//        if there are no records for the given department then log it as a message
        if(employeePage.getNumberOfElements() == 0){
            log.info("No employees found for department : {} on page : {}",department,pageable.getPageNumber());
            throw new ApiRequestException("No employees found for department: "+department+" on page: "+pageable.getPageNumber(),HttpStatus.NOT_FOUND);
        }

        return employeePage;
    }

    @Override
    public Page<Employee> findAllEmployees(Pageable pageable) {
        log.info("Pageable object :" + pageable);

//        Find all the employees in the specified page and size
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

//        if there are no records on the specified page then log it as a message
        if(employeePage.getNumberOfElements() == 0){
            log.info("No employees found on the specified page : {}",pageable.getPageNumber());
            throw new ApiRequestException("No employees found on page: "+pageable.getPageNumber(),HttpStatus.NOT_FOUND);
        }
        return employeePage;
    }

    @Override
    public void deleteEmployee(Long id) {
//        Check if the employee with the specified id exists
        Employee employee = findByEmpId(id);

//        If there are no employees with the specified id, log it as a message and return
        if (employee == null){
            log.info("deleteEmployee -> Employee does not exist");
            throw new ApiRequestException("No employee found for id: "+id,HttpStatus.BAD_REQUEST);
        }

//      Delete the employee that was found.
        employeeRepository.delete(employee);

//        log the message
        log.info("deleteEmployee -> Employee with id : {} has been deleted",id);
    }

    @Override
    public void deleteAllEmployees() {
//        delete all Employee records
        employeeRepository.deleteAll();
    }

    @Override
    public Employee updateEmployee(Long id, String name, String email, String department, String position, Integer salary) {

//      Check if the employee with the given id exists
        Employee employee = findByEmpId(id);

//      If employee is present update params as received from the request and return the update employee object
        if(employee != null){

//            if(params.containsKey("id") && params.get("id") != null){
//                employee.setId(((Integer)params.get("id")).longValue());
//            }
//                  --> Produces error on hibernate. Cant change primary key / identifier

            if(email!=null){
                Employee duplicate_employee = findByEmail(email);
                if(duplicate_employee == null){
                    if(email.equals(""))throw new ApiRequestException("Email cannot be empty",HttpStatus.BAD_REQUEST);
                    employee.setEmail(email);
                }
                else if(employee.getId() != id) {
                    log.info("employee with email : {} already exists",email);
                    throw new ApiRequestException("Employee with email: "+email+" already exists",HttpStatus.BAD_REQUEST); // try custome exception
                }
            }

            if(name!= null){
                if(name.equals("")) throw new ApiRequestException("Name cannot be empty",HttpStatus.BAD_REQUEST);
                employee.setName(name);
            }

            if (department!=null){
                if(department.equals("")) throw new ApiRequestException("Department cannot be empty",HttpStatus.BAD_REQUEST);
                employee.setDepartment(department);
            }
            if(position!=null){
                if(position.equals("")) throw new ApiRequestException("Position cannot be empty",HttpStatus.BAD_REQUEST);
                employee.setPosition(position);
            }
            if(salary!=null){
                employee.setSalary(salary);
            }
//            save the changes made to the employee
            employeeRepository.save(employee);

            return employee;
        }

//        Else log the message and return the null employee object
        else{
            log.info("updateEmployee -> No employee with id : {} exists",id);
            throw new ApiRequestException("No employee found for id : "+id,HttpStatus.BAD_REQUEST); // try custom exception
        }
    }

    @Override
    public Page<Employee> findByDepartmentAndPosition(String department, String position, Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findByDepartmentAndPosition(department,position,pageable);

//        if there are no records for the given department.
        if(employeePage.getNumberOfElements() == 0){
            log.info("No employees found for department : {}, position : {}, on page : {}",department,position,pageable.getPageNumber());
            throw new ApiRequestException("No employees found on page: "+pageable.getPageNumber(),HttpStatus.NOT_FOUND);
        }

        return employeePage;
    }

    @Override
    public Page<Employee> findBySalaryBetween(Pageable pageable, int minSalary, int maxSalary) {
        Page<Employee> employeePage = employeeRepository.findBySalaryBetween(minSalary, maxSalary, pageable);

//        if there are no records found for the given salary range.
        if(employeePage.getNumberOfElements() == 0){
            log.info("No employees found for salary range : {} - {}, on page : {}",minSalary,maxSalary,pageable.getPageNumber());
            throw new ApiRequestException("No employees found on page: "+pageable.getPageNumber(),HttpStatus.NOT_FOUND);
        }

        return employeePage;
    }


}
