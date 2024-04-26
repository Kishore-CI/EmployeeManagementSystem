package com.example.EmployeeManagementSystem.Controller;

import com.example.EmployeeManagementSystem.Model.Employee;
import com.example.EmployeeManagementSystem.Service.EmployeeService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmployeeController {

    private Logger log = LoggerFactory.getLogger(Employee.class);

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "api/v1/json/employee/saveEmployee", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> saveEmployee(@RequestParam String name, @RequestParam String email, @RequestParam String position,
                                       @RequestParam String department, @RequestParam int salary) throws MethodArgumentNotValidException {

        log.info("saveEmployee : Request received : " + name +" " + email + " "
        +department+" "+position+" "+salary);

//        Create the new employee object and save it to the database
        Employee new_employee = employeeService.saveEmployee(
                name, email, position, department, salary
        );
//        Return the response object
        return ResponseEntity.status(HttpStatus.CREATED).body(new_employee);
    }
}
