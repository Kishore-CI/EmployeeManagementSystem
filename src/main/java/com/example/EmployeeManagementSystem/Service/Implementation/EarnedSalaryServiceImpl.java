package com.example.EmployeeManagementSystem.Service.Implementation;

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
    public Double getEarnedSalary(Long id) {
//        log the request data
        log.info("getEarnedSalary -> {}",id);

//        check if the employee exists
        Employee employee = employeeService.findByEmpId(id);

//        if the employee is not found, return null
        if(employee == null){
            log.info("No employee found for id : {}",id);
            return null;
        }

//        check if the earned salary is already calculated for that employee
        EarnedSalary earnedSalaryRecord = earnedSalaryRepository.findByemployee(employee);

//        if not, calculate the earned salary
        if(earnedSalaryRecord == null){

            log.info("Calculating new earned salary record...");

            Double earnedSalary = calculateEarnedSalary(employee);

//            create the new earnedSalary record
            EarnedSalary newEarnedSalaryRecord = new EarnedSalary(earnedSalary,employee);

//            save the record
            earnedSalaryRepository.save(newEarnedSalaryRecord);

//            return the calculated earned salary
            return earnedSalary;
        }

        log.info("Earned salary record found : {}",earnedSalaryRecord.toString());

//        return the earned salary from the found record
        return earnedSalaryRecord.getEarned_salary();
    }
}
