package com.example.EmployeeManagementSystem.Service;

import com.example.EmployeeManagementSystem.Model.PaySlip;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.time.Month;
import java.time.Year;

public interface PaySlipService {


    /**
     * Generates payslip for all employees automatically on fixed time interval.
     * @throws IOException
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void autoGeneratePayslips() throws IOException;

    /**
     * Generates payslip for an employee holding the given id, for the give month and year after validation.
     * @param id
     * @param month
     * @param year
     * @return Payslip
     * @throws Exception
     */
    public PaySlip generatePaySlipForEmployee(Long id, Month month, Year year) throws Exception;
}
