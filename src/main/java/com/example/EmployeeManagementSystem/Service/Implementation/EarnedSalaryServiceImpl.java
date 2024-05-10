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

import java.util.List;

@Service
public class EarnedSalaryServiceImpl implements EarnedSalaryService {

    private Logger log = LoggerFactory.getLogger(EarnedSalaryService.class);

    @Autowired
    private EarnedSalaryRepository earnedSalaryRepository;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public Double calculateEarnedSalary(Employee employee) {
//        get the attendance list for the employee
        List<Attendance> attendanceList = attendanceService.getAttendanceForemployee(employee);

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
    public Double getEarnedSalary(Long id, boolean recalculate) {
//        log the request data
        log.info("getEarnedSalary -> {} {}",id, recalculate);

//        check if the employee exists
        Employee employee = employeeService.findByEmpId(id);

//        if the employee is not found, return null
        if(employee == null){
            log.info("No employee found for id : {}",id);
            throw new ApiRequestException("No employee found for id: "+id, HttpStatus.NOT_FOUND); // try custom exception
        }



//        check if the earned salary is already calculated for that employee
        EarnedSalary earnedSalaryRecord = earnedSalaryRepository.findByemployee(employee);

//        if not, create and save the record
        if(earnedSalaryRecord == null){

//        calculate the earnedSalary for the employee
            Double earnedSalary = calculateEarnedSalary(employee);

//            create the new earnedSalary record
            EarnedSalary newEarnedSalaryRecord = new EarnedSalary(earnedSalary,employee);

//            save the record
            earnedSalaryRepository.save(newEarnedSalaryRecord);

//            return the newly calculated earnedSalary
            return earnedSalary;

        }
//        if recalculate flag is true, then update the record with the newly calculated earnedSalary
        else if(recalculate){

//        calculate the earnedSalary for the employee
            Double earnedSalary = calculateEarnedSalary(employee);

//            update the earnedSalary record
            earnedSalaryRecord.setEarned_salary(earnedSalary);

//            save the updated record
            earnedSalaryRepository.save(earnedSalaryRecord);
        }

//        return the earned salary
        return earnedSalaryRecord.getEarned_salary();
    }
}
