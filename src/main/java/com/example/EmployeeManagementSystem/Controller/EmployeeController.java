package com.example.EmployeeManagementSystem.Controller;

import com.example.EmployeeManagementSystem.Exception.ApiRequestException;
import com.example.EmployeeManagementSystem.Model.Employee;
import com.example.EmployeeManagementSystem.Service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@Validated
public class EmployeeController {

    private Logger log = LoggerFactory.getLogger(Employee.class);

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "api/v1/json/employee/saveEmployee", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> saveEmployee(@Valid @RequestBody Map<String,Object> params) throws ApiRequestException {

//        log the request data
        log.info("saveEmployee : Request received : " + params);

//        Create the new employee object and save it to the database
        Employee new_employee = employeeService.saveEmployee(params);


//        Return the response object
        return ResponseEntity.status(HttpStatus.CREATED).body(new_employee);
    }

    @RequestMapping(value = "api/v1/json/employee/updateEmployee/", method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updateEmployee(@Valid @RequestParam long id, @Valid @RequestParam(required = false) String name,
                                            @Valid @RequestParam(required = false) String email, @Valid @RequestParam(required = false) String department,
                                            @Valid @RequestParam(required = false) String position,@Valid @RequestParam(required = false) Integer salary,
                                            @Valid @RequestParam(required = false) Long phone) throws ApiRequestException {

//        log the request data
        log.info("updateEmployee : Request Received : {} {} {} {} {} {}" ,id,name,email,department,position,salary);

//        Attempt updating of employee parameters
        Employee update_employee = employeeService.updateEmployee(id,name,email,department,position,salary,phone);

//        Return the appropriate HTTP Response based on the result

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(update_employee);

    }

    @RequestMapping(value = "api/v1/json/employee/findAllEmployees", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findAllEmployees(@Valid @RequestParam Integer page, @Valid @RequestParam Integer size) throws ApiRequestException{

//        log the request data
        log.info("findAllEmployees : Request Received : "+ page+size);

//        Find all employees in the given page with given page size
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Employee> employeePage = employeeService.findAllEmployees(pageRequest);

//        Return the response object with appropriate HTTP status
        return ResponseEntity.status(HttpStatus.OK).body(employeePage);

    }

    @RequestMapping(value = "api/v1/json/employee/findEmployee/", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findEmployeeById(@Valid @RequestParam Long id) throws ApiRequestException{
//        log the request data
        log.info("findEmployee : Request Received : {}",id);

//      find the employee
        Employee employee = employeeService.findEmployeeById(id);

//        Return Appropriate HTTP Response

        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @RequestMapping(value = "api/v1/json/employee/findEmployeeByDepartment", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findEmployeeByDepartment(@Valid @RequestParam String department, @Valid @PageableDefault Pageable pageable) throws ApiRequestException{
//        log the request data
        log.info("findEmployeeByDepartment : Request Received : {}",department);

//        find all the employees in the department
        Page<Employee> employeePage = employeeService.findByDepartment(department,pageable);


//        Return the response object with appropriate HTTP status

        return ResponseEntity.status(HttpStatus.OK).body(employeePage);
    }

    @RequestMapping(value = "api/v1/json/employee/findEmployeeByEmail", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findEmployeeByEmail(@Valid @RequestParam String email) throws ApiRequestException{
//        log the request data
        log.info("findEmployee : Request Received : {}",email);

//      find the employee
        Employee employee = employeeService.findByEmail(email);

//        Return Appropriate HTTP Response

        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @RequestMapping(value = "api/v1/json/employee/findEmployeeByDepartmentAndPosition", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findEmployeeByDepartmentAndPosition(@NotBlank @Valid @RequestParam String department, @Valid @RequestParam String position,
                                                                 @Valid @PageableDefault Pageable pageable) throws ApiRequestException{
//        log the request
        log.info("findEmployeeByDepartmentAndPosition : requestReceived : {} {}",department,position);
        Page<Employee> employeePage = employeeService.findByDepartmentAndPosition(department,position,pageable);

        return ResponseEntity.status(HttpStatus.OK).body(employeePage);
    }

    @RequestMapping(value = "api/v1/json/employee/findEmployeeByPhone", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findEmployeeByPhoneNumber(@Min(value = 1000000000L, message = "Phone number is below valid length")
                                                       @Max(value = 9999999999L, message = "Phone number is beyond valid length")
                                                       @Valid @RequestParam Long phone){
//        log the request
        log.info("findEmployeeByPhone : request received : {}",phone);

        Employee employee = employeeService.findEmployeeByPhoneNumber(phone);

        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @RequestMapping(value = "api/v1/json/employee/findEmployeeBySalaryRange", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findEmployeeBySalaryRange(@Valid @RequestParam Integer minSalary, @Valid @RequestParam Integer maxSalary,
                                                       @Valid @PageableDefault Pageable pageable) throws ApiRequestException{
//        log the request data
        log.info("findEmployeeBySalaryRange : requestReceived : {} {} {}",minSalary,maxSalary, pageable);

        Page<Employee> employeePage = employeeService.findBySalaryBetween(pageable,minSalary,maxSalary);

        return ResponseEntity.status(HttpStatus.OK).body(employeePage);
    }

    @RequestMapping(value = "api/v1/json/employee/deleteEmployee/", method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> deleteEmployee(@Valid @RequestParam Long id) throws ApiRequestException{
//        log the request data
        log.info("deleteEmployee : Request Received : " + id);

//        delete the employee
        employeeService.deleteEmployee(id);

//        return appropriate http response
        return ResponseEntity.status(HttpStatus.OK).body("Employee with id :"+id+" has been deleted");

    }

    @RequestMapping(value = "api/v1/json/employee/deleteAllEmployees", method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> deleteAllEmployees(){
//        log the request
        log.info("deleteAllEmployees : Request Received");

//      Delete All Employee records
        employeeService.deleteAllEmployees();
        return ResponseEntity.status(HttpStatus.OK).body("All Employee records have been deleted");
    }


}
