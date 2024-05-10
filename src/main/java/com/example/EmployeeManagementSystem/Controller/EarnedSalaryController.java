package com.example.EmployeeManagementSystem.Controller;

import com.example.EmployeeManagementSystem.Service.EarnedSalaryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@Validated
public class EarnedSalaryController {

    private Logger log = LoggerFactory.getLogger(EarnedSalaryController.class);
    @Autowired
    private EarnedSalaryService earnedSalaryService;

    @RequestMapping(value = "api/v1/json/earnedSalary/calculateEarnedSalary/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getEarnedSalary(@Valid @PathVariable("id") Long id,
                                             @Valid @RequestParam(defaultValue = "false") boolean recalculate){
//        log the request
        log.info("getEarnedSalary : Request Received : {}",id);
//        Calculate the earned salary
        Double earnedSalary = earnedSalaryService.getEarnedSalary(id,recalculate);
//        Return appropriate HTTP response
        return ResponseEntity.status(HttpStatus.OK).body(earnedSalary);
    }
}
