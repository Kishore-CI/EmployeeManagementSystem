package com.example.EmployeeManagementSystem.Thread;

import com.example.EmployeeManagementSystem.Model.Attendance;
import com.example.EmployeeManagementSystem.Model.Employee;
import com.example.EmployeeManagementSystem.Service.AttendanceService;
import com.example.EmployeeManagementSystem.Service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;


@Component
@Scope(value = "prototype")
public class AttendanceThread extends Thread{

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    @Lazy
    private AttendanceService attendanceService;

    private Logger log = LoggerFactory.getLogger(Employee.class);

    private String generationType;
    private Employee new_employee;

    public void setGenerationType(String generationType) {
        this.generationType = generationType;
    }

    public void setNew_employee(Employee new_employee) {
        this.new_employee = new_employee;
    }

    @Override
    public void run(){
        if(generationType.equals("ALL")){
            generateAttendanceForAll();
        }
        else if (generationType.equals("ONE")){
            generateAttendanceForOne(new_employee);
        }
    }

    private void generateAttendanceForOne(Employee employee) {

        LocalDate startDate = LocalDate.of(2024,4,1);
        LocalDate endDate = LocalDate.of(2024,6,1);
        Random random = new Random();

        for(LocalDate date = startDate;date.isBefore(endDate);date = date.plusDays(1)){
            boolean present = random.nextBoolean();
            Attendance attendanceRecord = new Attendance(employee,date,present);
            attendanceService.saveRecord(attendanceRecord);
        }

        log.info("Attendance population for employee id: {} is complete",employee.getId());
    }

    public void generateAttendanceForAll(){

        log.info("Starting attendance thread...");

        LocalDate startDate = LocalDate.of(2024,4,1);
        LocalDate endDate = LocalDate.of(2024,6,1);
        Random random = new Random();

        employeeService.findAll().forEach(employee->{
            log.info("Producing attendance for employee : {}",employee);
            for(LocalDate date = startDate;date.isBefore(endDate);date = date.plusDays(1)){
                boolean present = random.nextBoolean();
                Attendance attendanceRecord = new Attendance(employee,date,present);
                attendanceService.saveRecord(attendanceRecord);
            }
        });

        log.info("Attendance population for all employees is complete");
    }

}
