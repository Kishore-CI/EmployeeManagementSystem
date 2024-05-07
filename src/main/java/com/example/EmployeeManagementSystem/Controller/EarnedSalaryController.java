package com.example.EmployeeManagementSystem.Controller;

import com.example.EmployeeManagementSystem.Service.EarnedSalaryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Validated
public class EarnedSalaryController {

    private Logger log = LoggerFactory.getLogger(EarnedSalaryController.class);
    @Autowired
    private EarnedSalaryService earnedSalaryService;

    @RequestMapping(value = "api/v1/json/earnedSalary/calculateEarnedSalary/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getEarnedSalary(@Valid @PathVariable("id") Long id){
//        log the request
        log.info("getEarnedSalary : Request Received : {}",id);
//        Calculate the earned salary
        Double earnedSalary = earnedSalaryService.getEarnedSalary(id);
//        Return appropriate HTTP response
        if(earnedSalary == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No employee found for id : "+id);
        }
        return ResponseEntity.status(HttpStatus.OK).body(earnedSalary);
    }
}
