package com.example.EmployeeManagementSystem.Thread;

import com.example.EmployeeManagementSystem.Model.Employee;
import com.example.EmployeeManagementSystem.Repository.AttendanceRepository;
import com.example.EmployeeManagementSystem.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class AttendanceThreadStarter {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;


    public void startThread(String generationType, Employee employee){
        AttendanceThread attendanceThread = applicationContext.getBean(AttendanceThread.class);
        attendanceThread.setGenerationType(generationType);
        attendanceThread.setNew_employee(employee);
        attendanceThread.start();
        try {
            attendanceThread.join();
        } catch (InterruptedException e) {
           throw new RuntimeException(e);
        }
    }
}
