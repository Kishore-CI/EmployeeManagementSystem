package com.example.EmployeeManagementSystem.Service;

import com.example.EmployeeManagementSystem.Model.PaySlip;

import java.io.IOException;
import java.time.Month;
import java.time.Year;

public interface PaySlipService {


    PaySlip generatePaySlipForEmployee(Long id, Month month, Year year) throws Exception;
}
