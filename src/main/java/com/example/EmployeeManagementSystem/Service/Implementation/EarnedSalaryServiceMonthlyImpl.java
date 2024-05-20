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

//        calculate the total working days for which the employee was present
        Double totalDaysPresent = (double) attendanceList.stream().filter(Attendance::isPresent).count();

//        get the ideal salary of the employee
        Double ideal_salary = (double)employee.getSalary();

//        calculate the ratio of present days to total days
        Double salaryDaysRatio = (totalDaysPresent/31);

//        calculate the earned salary
        Double earnedSalary = (ideal_salary * salaryDaysRatio);

//        return the earned salary
        return earnedSalary;
    }

    @Override
    public Double getEarnedSalary(Long id, boolean recalculate, Optional<Month> month, Optional<LocalDate> startDate, Optional<LocalDate> endDate) {
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
        int year = 2024;
        Month monthValue;
        if (month.isPresent()) {
            monthValue = Month.valueOf(month.get().toString().toUpperCase());
        } else {
            throw new ApiRequestException("Month value cannot be null", HttpStatus.BAD_REQUEST);
        }
//        get the first and last days of the month
        YearMonth yearMonth = YearMonth.of(year, monthValue);
        LocalDate firsDay = LocalDate.of(year, monthValue, 1);
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
        }

//        return the earned salary
        return earnedSalaryRecord.getEarned_salary();
    }

    @Override
    public void deleteEarnedSalary(Long id, Month month) {
        Employee employee = employeeService.findByEmpId(id);
        if(employee == null){
            throw new ApiRequestException("No employee found for id: "+id,HttpStatus.NOT_FOUND);
        }
        EarnedSalary earnedSalary = earnedSalaryRepository.findByEmployeeAndMonth(employee,month);
        if(earnedSalary == null){
            throw new ApiRequestException("No earned salary record found for employee id: "+id+" on month: "+month,HttpStatus.NOT_FOUND);
        }
        earnedSalaryRepository.delete(earnedSalary);
    }

    @Override
    public void deleteAllEarnedSalary() {
        earnedSalaryRepository.deleteAll();
    }
}
