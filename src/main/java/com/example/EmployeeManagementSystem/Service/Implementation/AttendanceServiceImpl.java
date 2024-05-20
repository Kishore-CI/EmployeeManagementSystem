package com.example.EmployeeManagementSystem.Service.Implementation;

import com.example.EmployeeManagementSystem.Exception.ApiRequestException;
import com.example.EmployeeManagementSystem.Model.Attendance;
import com.example.EmployeeManagementSystem.Model.Employee;
import com.example.EmployeeManagementSystem.Repository.AttendanceRepository;
import com.example.EmployeeManagementSystem.Service.AttendanceService;
import com.example.EmployeeManagementSystem.Service.EarnedSalaryService;
import com.example.EmployeeManagementSystem.Service.EmployeeService;
import com.example.EmployeeManagementSystem.Thread.AttendanceThreadStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private Logger log = LoggerFactory.getLogger(AttendanceService.class);

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AttendanceThreadStarter attendanceThreadStarter;

    @Autowired
    @Lazy
    private Map<String, EarnedSalaryService> earnedSalaryServiceMap;


    @Override
    public List<Attendance> getAttendanceForemployee(Employee employee) {
//        find the attendanceList for the employee
        List<Attendance> attendanceList = attendanceRepository.findByemployee(employee);
        return attendanceList;
    }

    @Override
    public List<Attendance> getEmployeeAttendanceBetween(Employee employee, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendanceList = attendanceRepository.findByEmployeeAndDateBetween(employee,startDate,endDate);
        return attendanceList;
    }

    @Override
    public Attendance updateAttendanceForemployee(Long id, LocalDate date, boolean present) {
        Employee employee = findEmployee(id);
        if(employee == null){
            throw new ApiRequestException("No employee found for id: "+id,HttpStatus.NOT_FOUND);
        }
        Attendance attendanceRecord = findAttendanceRecord(employee,date);
        if(attendanceRecord == null){
            throw new ApiRequestException("No Attendance record found for employee id: "+id+" on date: "+date,HttpStatus.NOT_FOUND); // try custom exception
        }
        attendanceRecord.setPresent(present);
        return attendanceRepository.save(attendanceRecord);
    }

    @Override
    public List<Attendance> updateAttendanceForemployeeInBulk(Long id, LocalDate startDate, LocalDate endDate, boolean present) {
//        check if the employee exists
        Employee employee = findEmployee(id);

//        if the employee is not found return null
        if(employee == null){
            throw new ApiRequestException("No employee found for id: "+id,HttpStatus.NOT_FOUND);
        }

//        check if the start and end dates are same
        if(startDate.equals(endDate)){
            throw new ApiRequestException("Startdate cannot be equal to Enddate",HttpStatus.BAD_REQUEST);
        }

        if(endDate.isBefore(startDate)){
            throw new ApiRequestException("EndDate cannot be before StartDate",HttpStatus.BAD_REQUEST);
        }
//        check if the start date is valid
        if(findAttendanceRecord(employee,startDate) == null){
            throw new ApiRequestException("Invalid startDate: "+startDate,HttpStatus.BAD_REQUEST);// try custom exception
        }

//        check if the end date is valid
        if(findAttendanceRecord(employee,endDate) ==null){
            throw new ApiRequestException("Invalid endDate: "+endDate,HttpStatus.BAD_REQUEST);// try custom exception
        }

//        find the attendance records between the given dates
        List<Attendance> attendanceList = attendanceRepository.findByEmployeeAndDateBetween(employee,startDate,endDate);

//        update the attendance records with the given status
        for(Attendance attendance: attendanceList){
            attendance.setPresent(present);
            attendanceRepository.save(attendance);
        }
//        return the updated attendance list
        return attendanceList;
    }

    @Override
    public Attendance findAttendanceRecord(Employee employee, LocalDate date) {
        Attendance attendance = attendanceRepository.findByEmployeeAndDate(employee,date);
        if(attendance == null){
            log.info("No attendance record found for employee id : {} on date : {}",employee.getId(),date);
        }
        return attendance;
    }

    @Override
    public Employee findEmployee(Long id) {
        Employee employee = employeeService.findByEmpId(id);
        if(employee == null){
            log.info("No employee found for id : {}",id);
        }
        return employee;
    }

    @Override
    public List<Attendance> getAttendance(Long id) {
        Employee employee = findEmployee(id);
        if(employee == null){
            throw new ApiRequestException("No employee found for id: "+id, HttpStatus.NOT_FOUND); // try custom exception
        }
        return getAttendanceForemployee(employee);
    }

    @Override
    public void generateAttendanceForAll() {
//        log the request
        log.info("generateAttendance -> Request Received");

//        clear the old attendance records to prevent duplication
        attendanceRepository.deleteAll();

//        clear the old earned salary records to prevent incorrect salaries from being displayed and to maintain consistency with the new attendance records that will be generated.
        earnedSalaryServiceMap.get("earnedSalaryMonthly").deleteAllEarnedSalary();

//        provide initialization parameters and start the attendance thread
        attendanceThreadStarter.startThread("ALL",null);

    }

    @Override
    @Transactional
    public void generateAttendanceForEmployee(Long id){
        Employee employee = employeeService.findByEmpId(id);
        if(employee == null ){
            throw new ApiRequestException("No employee found for id: "+id,HttpStatus.NOT_FOUND);
        }
//            delete previous attendance records for employee
        attendanceRepository.deleteByemployee(employee);

//            delete previous earned salary records for that employee
        earnedSalaryServiceMap.get("earnedSalaryMonthly").deleteAllEarnedSalary();

//        provide initialization parameters and start the attendance thread
        attendanceThreadStarter.startThread("ONE",employee);

    }

    @Override
    @Transactional
    public void deleteAttendance(Long id, LocalDate date) {
        Employee employee = employeeService.findByEmpId(id);
        if(employee ==null){
            throw new ApiRequestException("No employee found for id: "+id,HttpStatus.NOT_FOUND);
        }
        Attendance attendance = findAttendanceRecord(employee,date);
        if(attendance == null){
            throw new ApiRequestException("No attendance record found for employee id: "+id+" on date: "+date,HttpStatus.NOT_FOUND);
        }
        attendanceRepository.delete(attendance);
    }

    @Override
    public void deleteAllAttendance() {
        attendanceRepository.deleteAll();
    }

    @Override
    @Transactional
    public void deleteAllAttendanceForEmployee(Long id){
        Employee employee = employeeService.findByEmpId(id);
        if(employee == null){
            throw new ApiRequestException("No employee found for id: "+id,HttpStatus.NOT_FOUND);
        }
        attendanceRepository.deleteByemployee(employee);
    }

    @Override
    public void saveRecord(Attendance attendanceRecord) {
//        log the request
        log.info("saveRecord -> Request Received {}",attendanceRecord);
//        save the record
        attendanceRepository.save(attendanceRecord);
    }
}
