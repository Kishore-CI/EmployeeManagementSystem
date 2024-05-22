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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Map;
import java.util.Optional;

@Controller
@Validated
public class EarnedSalaryController {

    private Logger log = LoggerFactory.getLogger(EarnedSalaryController.class);
    @Autowired
    private Map<String,EarnedSalaryService> earnedSalaryService;

    @RequestMapping(value = "api/v1/json/earnedSalary/calculateForMonth/", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getEarnedSalaryForMonth(@Valid @RequestParam Long id,
                                                     @Valid @RequestParam(defaultValue = "false") boolean recalculate,
                                                     @Valid @RequestParam Month month,
                                                     @Valid @RequestParam Year year) throws IllegalArgumentException{
//        log the request
        log.info("getEarnedSalaryForMonth : Request Received : {}",id);
//        Calculate the earned salary
        Double earnedSalary = earnedSalaryService.get("earnedSalaryMonthly").getEarnedSalary(id,recalculate, Optional.of(month),Optional.of(year),Optional.empty(),Optional.empty());
//        Return appropriate HTTP response
        return ResponseEntity.status(HttpStatus.OK).body(earnedSalary);
    }

    @RequestMapping(value = "api/v1/json/earnedSalary/calculateBetween/", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getEarnedSalaryBetween(@Valid @RequestParam Long id,
                                                    @Valid @RequestParam LocalDate startDate,
                                                    @Valid @RequestParam LocalDate endDate) throws IllegalArgumentException{
//        log the request
        log.info("getEarnedSalaryBetween : Request Received : {} {} {}",id,startDate,endDate);
//        Calculate the earned salary
        Double earnedSalary = earnedSalaryService.get("earnedSalaryDaily").getEarnedSalary(id,false, Optional.empty(),Optional.empty(),Optional.of(startDate),Optional.of(endDate));
//        Return appropriate HTTP response
        return ResponseEntity.status(HttpStatus.OK).body(earnedSalary);
    }
}
