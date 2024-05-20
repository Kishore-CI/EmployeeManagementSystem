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

    private LocalDate startDate ;
    private LocalDate endDate ;
    Random random = new Random();

    public void setGenerationType(String generationType) {
        this.generationType = generationType;
    }

    public void setNew_employee(Employee new_employee) {
        this.new_employee = new_employee;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

        for(LocalDate date = startDate;!date.isAfter(endDate);date = date.plusDays(1)){
            boolean present = random.nextBoolean();
            Attendance attendanceRecord = new Attendance(employee,date,present);
            attendanceService.saveRecord(attendanceRecord);
        }

        log.info("Attendance population for employee id: {} is complete",employee.getId());
    }

    public void generateAttendanceForAll(){

        log.info("Starting attendance thread...");

        employeeService.findAll().forEach(employee->{
            log.info("Producing attendance for employee : {}",employee);
            for(LocalDate date = startDate;!date.isAfter(endDate);date = date.plusDays(1)){
                boolean present = random.nextBoolean();
                Attendance attendanceRecord = new Attendance(employee,date,present);
                attendanceService.saveRecord(attendanceRecord);
            }
        });

        log.info("Attendance population for all employees is complete");
    }

}
