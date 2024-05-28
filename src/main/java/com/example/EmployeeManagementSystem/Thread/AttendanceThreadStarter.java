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
//        Initialize a new attendance thread object
        AttendanceThread attendanceThread = attendanceThreadObjectFactory.getObject();
//        sets the generation type
        attendanceThread.setGenerationType(generationType);

//        sets the employee object
        attendanceThread.setNew_employee(employee);

//        sets the start date
        attendanceThread.setStartDate(startDate);

//        sets the end date
        attendanceThread.setEndDate(endDate);

//        starts the thread
        attendanceThread.start();

//      Waits for thread execution completion
        try {
            attendanceThread.join();
        } catch (InterruptedException e) {
           throw new RuntimeException(e);
        }
    }
}
