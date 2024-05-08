package com.example.EmployeeManagementSystem.Thread;

import com.example.EmployeeManagementSystem.Model.Attendance;
import com.example.EmployeeManagementSystem.Model.Employee;
import com.example.EmployeeManagementSystem.Repository.AttendanceRepository;
import com.example.EmployeeManagementSystem.Repository.EmployeeRepository;
import org.hibernate.validator.internal.util.stereotypes.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;


@Component
public class AttendanceThread extends Thread{

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    @Autowired
    public AttendanceThread(EmployeeRepository employeeRepository, AttendanceRepository attendanceRepository){
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
    }

    private Logger log = LoggerFactory.getLogger(Employee.class);

    @Override
    public void run(){

        log.info("Starting attendance thread...");

        LocalDate startDate = LocalDate.of(2024,5,1);
        LocalDate endDate = LocalDate.of(2024,6,1);
        Random random = new Random();

        employeeRepository.findAll().stream().forEach(employee->{
            log.info("Producing attendance for employee : {}",employee);
            for(LocalDate date = startDate;date.isBefore(endDate);date = date.plusDays(1)){
                boolean present = random.nextBoolean();
                Attendance attendanceRecord = new Attendance(employee,date,present);
                attendanceRepository.save(attendanceRecord);
            }
        });

        log.info("Attendance population for employees is complete");

    }

}
