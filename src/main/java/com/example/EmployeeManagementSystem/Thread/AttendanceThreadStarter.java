package com.example.EmployeeManagementSystem.Thread;

import com.example.EmployeeManagementSystem.Model.Employee;
import com.example.EmployeeManagementSystem.Repository.AttendanceRepository;
import com.example.EmployeeManagementSystem.Repository.EmployeeRepository;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class AttendanceThreadStarter {

    @Autowired
    private ObjectFactory<AttendanceThread> attendanceThreadObjectFactory;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;


    public void startThread(String generationType, Employee employee, LocalDate startDate, LocalDate endDate){
        AttendanceThread attendanceThread = attendanceThreadObjectFactory.getObject();
        attendanceThread.setGenerationType(generationType);
        attendanceThread.setNew_employee(employee);
        attendanceThread.setStartDate(startDate);
        attendanceThread.setEndDate(endDate);
        attendanceThread.start();
        try {
            attendanceThread.join();
        } catch (InterruptedException e) {
           throw new RuntimeException(e);
        }
    }
}
