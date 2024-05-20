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
        Employee new_employee = new Employee();

//        Check if an email parameter is present in the params and if it is valid
        if(params.containsKey("email")) {
            var email = params.get("email");
            if(email == null){
                throw new ApiRequestException("Employee's email cannot be null", HttpStatus.BAD_REQUEST);
            }
            if (email == "") {
                throw new ApiRequestException("Employee's email cannot be empty",HttpStatus.BAD_REQUEST);
            }
            if(!(email instanceof String)){
                throw new ApiRequestException("Employee's email is not of valid format. Required format: String",HttpStatus.BAD_REQUEST);
            }

//          Check if the employee already exists
            Employee duplicate_employee = findByEmail((String) email);

//              If employee exists then log it and return an empty employee
            if (duplicate_employee != null) {
                log.info("saveEmployee -> Employee with email : {} already exists", email);
                throw new ApiRequestException("Employee with email : " + email + " already exists", HttpStatus.BAD_REQUEST); // try custome exception
            }
//            provide the email attribute value for the new employee
            new_employee.setEmail((String) email);
        }
        else{
            throw new ApiRequestException("Employee's email is required",HttpStatus.BAD_REQUEST);
        }

//        check if a phone number parameter is present in the params map and if it is valid
        if(params.containsKey("phone")){
            var phone = params.get("phone");
            if(phone == null){
                throw new ApiRequestException("Employee's phone number cannot be null",HttpStatus.BAD_REQUEST);
            }
            if(phone instanceof Integer){
                throw new ApiRequestException("Employee's phone number length is below required length. Required length: 10",HttpStatus.BAD_REQUEST);
            }
            if(!(phone instanceof  Long)){
                throw new ApiRequestException("Employee's phone number is of invalid format. Require format: Long",HttpStatus.BAD_REQUEST);
            }

//            check if the phone number is already associated with another employee
            Employee duplicate_employee = findByPhoneNumber((Long) phone);

            if(duplicate_employee != null){
                log.info("saveEmployee -> Employee with phone number: {} already exists",phone);
                throw new ApiRequestException("Employee with phone number: "+phone+" already exists",HttpStatus.BAD_REQUEST);
            }

//            provide the phone number value for the new employee
            new_employee.setPhone((Long) phone);
        }
        else{
            throw new ApiRequestException("Employee phone number is required",HttpStatus.BAD_REQUEST);
        }

//        Validate and assign the remaining attributes to the new employee
        var name = params.get("name");
        if(name != null){
            if(name.equals("")) {
                throw new ApiRequestException("Employee's Name cannot be null or empty", HttpStatus.BAD_REQUEST);
            }
            if(!(name instanceof String)){
                throw new ApiRequestException("Employee's Name is of invalid type. Type should be : String ",HttpStatus.BAD_REQUEST);
            }
            new_employee.setName((String) name);
        }
        else throw new ApiRequestException("Employee's name cannot be null",HttpStatus.BAD_REQUEST);

        var department = params.get("department");
        if(department != null){
            if(department.equals("")) {
                throw new ApiRequestException("Employee's department cannot be empty", HttpStatus.BAD_REQUEST);
            }
            if(!(department instanceof String)){
                throw new ApiRequestException("Employee's department is of invalid type. Type should be : String ",HttpStatus.BAD_REQUEST);
            }
            new_employee.setDepartment((String) department);
        }
        else throw new ApiRequestException("Employee's department cannot be null",HttpStatus.BAD_REQUEST);

        var position = params.get("position");
        if(position != null){
            if(position.equals("")) {
                throw new ApiRequestException("Employee's position cannot be empty", HttpStatus.BAD_REQUEST);
            }
            if(!(position instanceof String)){
                throw new ApiRequestException("Employee's position is of invalid type. Type should be : String ",HttpStatus.BAD_REQUEST);
            }
            new_employee.setPosition((String) position);
        }
        else throw new ApiRequestException("Employee's position cannot be null",HttpStatus.BAD_REQUEST);

        var salary = params.get("salary");
        if(salary != null){
            if(!(salary instanceof Integer)){
                throw new ApiRequestException("Employee's Salary is of invalid type. Type should be : Integer ",HttpStatus.BAD_REQUEST);
            }
            new_employee.setSalary((Integer) salary);
        }
        else throw new ApiRequestException("Employee's Salary cannot be null",HttpStatus.BAD_REQUEST);


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
    public Employee updateEmployee(Long id, String name, String email, String department, String position, Integer salary, Long phone) {

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
                    if(email.isEmpty())throw new ApiRequestException("Email cannot be empty",HttpStatus.BAD_REQUEST);
                    employee.setEmail(email);
                }
//                to ensure that giving the current email address for the same employee does not count as an exception case
                else if(employee.getId() != id) {
                    log.info("employee with email : {} already exists",email);
                    throw new ApiRequestException("Employee with email: "+email+" already exists",HttpStatus.BAD_REQUEST); // try custome exception
                }
            }

            if(phone!=null){

                Employee duplicate_employee = findByPhoneNumber(phone);
                if(duplicate_employee == null){
                    employee.setPhone(phone);
                }
//                to ensure that giving the current phone number for the same employee does not count as an exception case
                else if (employee.getId() != id) {
                    log.info("Employee with phone number : {} already exists",phone);
                    throw new ApiRequestException("Employe with phone number: "+phone+" already exists.",HttpStatus.BAD_REQUEST);
                }

            }

            if(name!= null){
                if(name.isEmpty()) throw new ApiRequestException("Name cannot be empty",HttpStatus.BAD_REQUEST);
                employee.setName(name);
            }

            if (department!=null){
                if(department.isEmpty()) throw new ApiRequestException("Department cannot be empty",HttpStatus.BAD_REQUEST);
                employee.setDepartment(department);
            }
            if(position!=null){
                if(position.isEmpty()) throw new ApiRequestException("Position cannot be empty",HttpStatus.BAD_REQUEST);
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

    @Override
    public Employee findByPhoneNumber(Long phone) {
        Employee employee = employeeRepository.findByphone(phone);

        if(employee==null){
            log.info("findByPhoneNumber -> No employee with phone number: {}",phone);
        }

        return employee;
    }

    @Override
    public Employee findEmployeeByPhoneNumber(Long phone) {
        Employee employee = findByPhoneNumber(phone);

        if(employee == null){
            throw new ApiRequestException("No employee with phone number: "+phone,HttpStatus.BAD_REQUEST);
        }

        return employee;
    }


}
