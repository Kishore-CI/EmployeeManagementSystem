package com.example.EmployeeManagementSystem.Service.Implementation;

import com.example.EmployeeManagementSystem.Exception.ApiRequestException;
import com.example.EmployeeManagementSystem.Model.Attendance;
import com.example.EmployeeManagementSystem.Model.EarnedSalary;
import com.example.EmployeeManagementSystem.Model.Employee;
import com.example.EmployeeManagementSystem.Repository.EarnedSalaryRepository;
import com.example.EmployeeManagementSystem.Service.AttendanceService;
import com.example.EmployeeManagementSystem.Service.EarnedSalaryService;
import com.example.EmployeeManagementSystem.Service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service("earnedSalaryMonthly")
public class EarnedSalaryServiceMonthlyImpl implements EarnedSalaryService {

    private Logger log = LoggerFactory.getLogger(EarnedSalaryService.class);

    @Autowired
    private EarnedSalaryRepository earnedSalaryRepository;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public Double calculateEarnedSalary(Employee employee, LocalDate startdate, LocalDate endDate) {
//        get the attendance list for the employee
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

//        return the earned salary
        return earnedSalary;
    }

    @Override
    public Double getEarnedSalary(Long id, boolean recalculate, Optional<Month> month, Optional<Year> year, Optional<LocalDate> startDate, Optional<LocalDate> endDate) {
//        log the request data
        log.info("getEarnedSalary -> {} {}", id, recalculate);

//        check if the employee exists
        Employee employee = employeeService.findByEmpId(id);

//        if the employee is not found, return null
        if (employee == null) {
            log.info("No employee found for id : {}", id);
            throw new ApiRequestException("No employee found for id: " + id, HttpStatus.NOT_FOUND); // try custom exception
        }
//        Check if the month value from the request is valid
        Year yearValue = year.get();
        Month monthValue;
        if (month.isPresent()) {
            monthValue = Month.valueOf(month.get().toString().toUpperCase());
        } else {
            throw new ApiRequestException("Month value cannot be null", HttpStatus.BAD_REQUEST);
        }
//        get the first and last days of the month
        YearMonth yearMonth = YearMonth.of(yearValue.getValue(), monthValue);
        LocalDate firsDay = LocalDate.of(yearValue.getValue(), monthValue, 1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

//        Check if there are attendance records for the first and last days
        Attendance firstDayRecord = attendanceService.findAttendanceRecord(employee, firsDay);
        Attendance lastDayRecord = attendanceService.findAttendanceRecord(employee, lastDay);
        if (firstDayRecord == null || lastDayRecord == null) {
            throw new ApiRequestException("Attendance records are not found for the specified month: "+month.get()+" for Employee id: " + id, HttpStatus.NOT_FOUND);
        }

//        check if the earned salary is already calculated for that employee
        EarnedSalary earnedSalaryRecord = earnedSalaryRepository.findByEmployeeAndMonth(employee, monthValue);

//        if not, create and save the record
        if (earnedSalaryRecord == null) {

//        calculate the earnedSalary for the employee
            Double earnedSalary = calculateEarnedSalary(employee, firsDay, lastDay);

//            create the new earnedSalary record
            EarnedSalary newEarnedSalaryRecord = new EarnedSalary(earnedSalary, employee, monthValue);

//            save the record
            earnedSalaryRepository.save(newEarnedSalaryRecord);

            log.info("getEarnedSalary -> Record created: {}",newEarnedSalaryRecord);

//            return the newly calculated earnedSalary
            return earnedSalary;

        }
//        if recalculate flag is true, then update the record with the newly calculated earnedSalary
        else if (recalculate) {

//        calculate the earnedSalary for the employee
            Double earnedSalary = calculateEarnedSalary(employee, firsDay, lastDay);

//            update the earnedSalary record
            earnedSalaryRecord.setEarned_salary(earnedSalary);

//            update the monthValue
            earnedSalaryRecord.setMonth(monthValue);

//            save the updated record
            earnedSalaryRepository.save(earnedSalaryRecord);
            log.info("getEarnedSalary -> Record updated after recalculation: {}",earnedSalaryRecord);
        }

//        return the earned salary
        log.info("getEarnedSalary -> Record found: {}",earnedSalaryRecord);
        return earnedSalaryRecord.getEarned_salary();
    }

    @Override
    public void deleteEarnedSalary(Long id, Month month) {
//        checks if the employee exists
        Employee employee = employeeService.findByEmpId(id);
        if(employee == null){
            throw new ApiRequestException("No employee found for id: "+id,HttpStatus.NOT_FOUND);
        }
//        checks if the earned salary record exists
        EarnedSalary earnedSalary = earnedSalaryRepository.findByEmployeeAndMonth(employee,month);
        if(earnedSalary == null){
            throw new ApiRequestException("No earned salary record found for employee id: "+id+" on month: "+month,HttpStatus.NOT_FOUND);
        }
//        deletes the record
        earnedSalaryRepository.delete(earnedSalary);
    }

    @Override
    public void deleteAllEarnedSalary() {
//        deletes all the earned salary records
        earnedSalaryRepository.deleteAll();
        log.info("deleteAllEarnedSalary -> completed");
    }
}
