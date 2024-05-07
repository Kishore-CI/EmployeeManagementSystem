package com.example.EmployeeManagementSystem.Thread;

import com.example.EmployeeManagementSystem.Repository.AttendanceRepository;
import com.example.EmployeeManagementSystem.Repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AttendanceThreadStarter {

    @Autowired
    private AttendanceThread attendanceThread;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    public void startThread(){
        attendanceThread.start();
        try {
            attendanceThread.join();
        } catch (InterruptedException e) {
           throw new RuntimeException(e);
        }
    }
}
