package com.example.EmployeeManagementSystem.Service.Implementation;

import com.example.EmployeeManagementSystem.Exception.ApiRequestException;
import com.example.EmployeeManagementSystem.Model.Attendance;
import com.example.EmployeeManagementSystem.Model.EarnedSalary;
import com.example.EmployeeManagementSystem.Model.Employee;
import com.example.EmployeeManagementSystem.Repository.EarnedSalaryRepository;
import com.example.EmployeeManagementSystem.Service.AttendanceService;
import com.example.EmployeeManagementSystem.Service.EarnedSalaryService;
import com.example.EmployeeManagementSystem.Service.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service("earnedSalaryDaily")
public class EarnedSalaryServiceDailyImpl implements EarnedSalaryService {

    private Logger log = LoggerFactory.getLogger(EarnedSalaryServiceDailyImpl.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private EarnedSalaryRepository earnedSalaryRepository;


    @Override
    public Double calculateEarnedSalary(Employee employee, LocalDate startdate, LocalDate endDate) {

//        get the attendance list for the employee between the specified dates
        List<Attendance> attendanceList = attendanceService.getEmployeeAttendanceBetween(employee,startdate,endDate);

//        Calculate the total days in the month
        Integer totalDaysInMonth = endDate.lengthOfMonth();

//        Calculate the Daily salary of that employee
        Double dailySalary = (double) employee.getSalary()/ (double) totalDaysInMonth;
        dailySalary = Math.round(dailySalary * 100.0) / 100.0;

//        Calculate the total days for which employee was present
        Double totalDaysPresent = (double) attendanceList.stream().filter(Attendance::isPresent).count();

//        Calculte the salary earned for days present
        Double earnedSalary = totalDaysPresent * dailySalary;

        log.info("calculateEarnedSalary: {}",earnedSalary);

//        return the calculate salary earned
        return earnedSalary;
    }

    @Override
    public Double getEarnedSalary(Long id, boolean recalculate, Optional<Month> month, Optional<Year> year, Optional<LocalDate> startDate, Optional<LocalDate> endDate) {
//        log the request
        log.info("getEarnedSalary -> Request Received: {} {} {} {}",id,recalculate,startDate,endDate);

//        Check if the employee exists
        Employee employee = employeeService.findByEmpId(id);
        if(employee==null){
            throw new ApiRequestException("No employee found for id: "+id, HttpStatus.NOT_FOUND);
        }

//        Check if start and end dates are present in the request
        if(startDate.isEmpty()){
            throw new ApiRequestException("Start date cannot be empty",HttpStatus.BAD_REQUEST);
        }
        if(endDate.isEmpty()){
            throw new ApiRequestException("End Date cannot be empty",HttpStatus.BAD_REQUEST);
        }

//        Check if the start date is before end date
        if(startDate.get().isAfter(endDate.get())){
            throw new ApiRequestException("End date cannot be before start date",HttpStatus.BAD_REQUEST);
        }

//        Check if there are attendance records for start and end dates
        Attendance attendanceStart = attendanceService.findAttendanceRecord(employee,startDate.get());
        if(attendanceStart == null){
            throw new ApiRequestException("No attendance record found for startDate: "+startDate.get(),HttpStatus.NOT_FOUND);
        }
        Attendance attendanceEnd = attendanceService.findAttendanceRecord(employee,endDate.get());
        if(attendanceEnd == null){
            throw new ApiRequestException("No attendance record found for endDate: "+endDate.get(),HttpStatus.NOT_FOUND);
        }

//        Calculate the salary between the specified dates
        Double earnedSalary = calculateEarnedSalary(employee,startDate.get(),endDate.get());

//        return the calculated salary
        return earnedSalary;
    }

    @Override
    public void deleteEarnedSalary(Long id, Month month) {

    }

    @Override
    public void deleteAllEarnedSalary() {
        earnedSalaryRepository.deleteAll();
        log.info("deleteAllEarnedSalary -> completed");
    }
}
