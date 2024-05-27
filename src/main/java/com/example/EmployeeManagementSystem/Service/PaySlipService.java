package com.example.EmployeeManagementSystem.Service;

import com.example.EmployeeManagementSystem.Model.PaySlip;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.time.Month;
import java.time.Year;

public interface PaySlipService {


    @Scheduled(cron = "0 */5 * * * *")
    void autoGeneratePayslips() throws IOException;

    PaySlip generatePaySlipForEmployee(Long id, Month month, Year year) throws Exception;
}
