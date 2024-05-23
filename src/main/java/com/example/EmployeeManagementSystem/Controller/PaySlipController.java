package com.example.EmployeeManagementSystem.Controller;

import com.example.EmployeeManagementSystem.Model.PaySlip;
import com.example.EmployeeManagementSystem.Service.PaySlipService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Month;
import java.time.Year;

@Controller
@Validated
public class PaySlipController {

    @Autowired
    private PaySlipService paySlipService;

    private Logger log = LoggerFactory.getLogger(PaySlipController.class);

    @RequestMapping(value = "api/v1/json/payslip/generatePaySlip/", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> generatePaySlip(@Valid @RequestParam Long id, @Valid @RequestParam Month month,
                                             @Valid @RequestParam Year year) throws Exception {
//        log the request
        log.info("generatePaySlip : request received : {} {} {}",id,month,year);

//        generate the payslip
        PaySlip paySlip = paySlipService.generatePaySlipForEmployee(id,month,year);

//        Set the Http headers for the response
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=document.pdf");
        httpHeaders.add("Employee Name",paySlip.getEmployee().getName());
        httpHeaders.add("Employee Id",String.valueOf(paySlip.getEmployee().getId()));
        httpHeaders.add("Pay Period",paySlip.getMonth()+" "+paySlip.getYear());
        httpHeaders.add("Content-Type",MediaType.APPLICATION_PDF.toString());

//        return the appropriate http response
        return new ResponseEntity<>(paySlip.getPayslipdata(), httpHeaders, HttpStatus.OK);
    }
}
