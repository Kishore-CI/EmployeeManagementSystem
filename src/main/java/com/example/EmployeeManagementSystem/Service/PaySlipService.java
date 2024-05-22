package com.example.EmployeeManagementSystem.Service;

import java.io.IOException;
import java.time.Month;
import java.time.Year;

public interface PaySlipService {


    byte[] generatePaySlipForEmployee(Long id, Month month, Year year) throws Exception;
}
