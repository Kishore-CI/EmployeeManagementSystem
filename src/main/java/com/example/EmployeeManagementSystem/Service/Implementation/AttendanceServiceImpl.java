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
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.Arrays;
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
        log.info("getAttendanceForemployee: {}", Arrays.toString(attendanceList.toArray()));
        return attendanceList;
    }

    @Override
    public List<Attendance> getEmployeeAttendanceBetween(Employee employee, LocalDate startDate, LocalDate endDate) {
//        finds the attendance record for an employee between the given dates
        List<Attendance> attendanceList = attendanceRepository.findByEmployeeAndDateBetween(employee,startDate,endDate);
        log.info("getEmployeeAttendanceBetween: {}",Arrays.toString(attendanceList.toArray()));
        return attendanceList;
    }

    @Override
    public Attendance updateAttendanceForemployee(Long id, LocalDate date, boolean present) {
//        Checks if the employee exists
        Employee employee = findEmployee(id);
        if(employee == null){
            throw new ApiRequestException("No employee found for id: "+id,HttpStatus.NOT_FOUND);
        }
//        checks if the attendance record exists
        Attendance attendanceRecord = findAttendanceRecord(employee,date);
        if(attendanceRecord == null){
            throw new ApiRequestException("No Attendance record found for employee id: "+id+" on date: "+date,HttpStatus.NOT_FOUND); // try custom exception
        }
//        Updates the present value
        attendanceRecord.setPresent(present);
        log.info("updateAttendanceForemployee: {}",attendanceRecord);
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

        log.info("updateAttendanceForemployeeInBulk: {}",Arrays.toString(attendanceList.toArray()));
//        return the updated attendance list
        return attendanceList;
    }

    @Override
    public Attendance findAttendanceRecord(Employee employee, LocalDate date) {
        Attendance attendance = attendanceRepository.findByEmployeeAndDate(employee,date);
        if(attendance == null){
            log.info("No attendance record found for employee id : {} on date : {}",employee.getId(),date);
        }
        log.info("findAttendanceRecord : {}",attendance);
        return attendance;
    }

//    utility method that finds an employee by the given id
    private Employee findEmployee(Long id) {
        Employee employee = employeeService.findByEmpId(id);
        if(employee == null){
            log.info("No employee found for id : {}",id);
        }
        log.info("findEmployee: {}",employee);
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
    public List<Attendance> generateAttendanceForAll(LocalDate startDate, LocalDate endDate) {
//        log the request
        log.info("generateAttendance -> Request Received");

//        check if the start date is before end date
        if(endDate.isBefore(startDate)){
            throw new ApiRequestException("endDate cannot be before startDate",HttpStatus.BAD_REQUEST);
        }

//        clear the old attendance records to prevent duplication
        attendanceRepository.deleteAll();

//        clear the old earned salary records to prevent incorrect salaries from being displayed and to maintain consistency with the new attendance records that will be generated.
        earnedSalaryServiceMap.get("earnedSalaryMonthly").deleteAllEarnedSalary();

//        provide initialization parameters and start the attendance thread
        attendanceThreadStarter.startThread("ALL",null,startDate, endDate);

//        fetch the newly generated attendance for all employees and return it
        List<Attendance> attendanceList = attendanceRepository.findAll();

        log.info("generateAttendanceForAll: {}",attendanceList);

        return attendanceList;
    }

    @Override
    public List<Attendance> generateAttendanceForEmployee(Long id,LocalDate startDate, LocalDate endDate){
        Employee employee = employeeService.findByEmpId(id);
        if(employee == null){
            employee = employeeService.findEmployeeByPhoneNumber(id);
            if(employee == null){
                throw new ApiRequestException("No employee found for id / mobile number: "+id,HttpStatus.NOT_FOUND);
            }
        }

//        check if the start date is before end date
        if(endDate.isBefore(startDate)){
            throw new ApiRequestException("endDate cannot be before startDate",HttpStatus.BAD_REQUEST);
        }

//            delete previous attendance records for employee
        deleteEmployeeAttendanceBetween(employee,startDate,endDate);

//            delete previous earned salary records for that employee
        earnedSalaryServiceMap.get("earnedSalaryMonthly").deleteAllEarnedSalary();

//        provide initialization parameters and start the attendance thread
        attendanceThreadStarter.startThread("ONE",employee,startDate, endDate);

//        get the newly generated attendance for the employee
        List<Attendance> attendanceList = attendanceRepository.findByEmployeeAndDateBetween(employee,startDate,endDate);

        log.info("generateAttendanceForEmployee: {}",attendanceList);

//        return the attendance list
        return attendanceList;
    }

    @Override
    @Transactional
    public void deleteAttendance(Long id, LocalDate date) {
//        checks if the employee exists
        Employee employee = employeeService.findByEmpId(id);
        if(employee ==null){
            throw new ApiRequestException("No employee found for id: "+id,HttpStatus.NOT_FOUND);
        }
//        checks if the attendance records exist
        Attendance attendance = findAttendanceRecord(employee,date);
        if(attendance == null){
            throw new ApiRequestException("No attendance record found for employee id: "+id+" on date: "+date,HttpStatus.NOT_FOUND);
        }
//        deletes the attendance record
        attendanceRepository.delete(attendance);
        log.info("deleteAttendance: {}",attendance);
    }

    @Override
    public void deleteAllAttendance() {
//        deletes all the attendance for the all the employees
        attendanceRepository.deleteAll();
        log.info("deleteAllAttendance -> Completed");
    }

    @Override
    @Transactional
    public void deleteAllAttendanceForEmployee(Long id){
//        check if the employee exists
        Employee employee = employeeService.findByEmpId(id);

        if(employee == null){
            throw new ApiRequestException("No employee found for id: "+id,HttpStatus.NOT_FOUND);
        }

//        Deletes the records for the employee
        attendanceRepository.deleteByemployee(employee);

        log.info("deleteAllAttendanceForEmployee -> completed for: {}",employee);
    }

    @Override
    public void saveRecord(Attendance attendanceRecord) {
//        log the request

//        save the record
        attendanceRepository.save(attendanceRecord);

        log.info("savedRecord -> completed: {}",attendanceRecord);
    }

    @Override
    public Long findTotalDaysPresentInMonth(Long id, Month month, Year year) {
        Employee employee = employeeService.findByEmpId(id);
        if (employee == null){
            throw new ApiRequestException("No employee found for id: "+id,HttpStatus.NOT_FOUND);
        }

//        construct the start and end dates of the month
        YearMonth yearMonth = YearMonth.of(year.getValue(),month.getValue());
        LocalDate startDate = LocalDate.of(year.getValue(),month.getValue(),1);
        LocalDate endDate = yearMonth.atEndOfMonth();

//        check if the attendance records exist for the start and end of the month
        Attendance startDay = findAttendanceRecord(employee,startDate);
        if(startDay == null){
            throw new ApiRequestException("No attendance record found for employee id: "+id+" for start of the month :"+month+" on year: "+year,HttpStatus.NOT_FOUND);
        }
        Attendance endDay = findAttendanceRecord(employee,endDate);
        if(endDay == null){
            throw new ApiRequestException("No attendance record found  for employee id: "+id+" for end of the month :"+month+" on year: "+year,HttpStatus.NOT_FOUND);
        }

//        fetch all the attendance record for the given month
        List<Attendance> attendanceList = getEmployeeAttendanceBetween(employee,startDate,endDate);

//        count total days present for the month
        Long totalDaysPresent = attendanceList.stream().filter(Attendance::isPresent).count();

        log.info("findTotalDaysPresentInMonth : {} {} {}",employee,yearMonth,totalDaysPresent);

//        return the value
        return totalDaysPresent;
    }

    @Override
    public Long findTotalDaysPresentInYear(Long id, Year year) {
//        check if the employee exists
        Employee employee = employeeService.findByEmpId(id);

        if(employee == null){
            throw new ApiRequestException("No employee found for id: "+id,HttpStatus.NOT_FOUND);
        }

//        construct the start and end dates of the year
        LocalDate startDate = LocalDate.of(year.getValue(),1,1);
        LocalDate endDate = LocalDate.of(year.getValue(),12,31);

//        check if the attendance records exist for the start and end of the month
        Attendance startDay = findAttendanceRecord(employee,startDate);
        if(startDay == null){
            throw new ApiRequestException("No attendance record found for employee id: "+id+"  for start of the year :"+year,HttpStatus.NOT_FOUND);
        }
        Attendance endDay = findAttendanceRecord(employee,endDate);
        if(endDay == null){
            throw new ApiRequestException("No attendance record found for employee id: "+id+" for end of the year :"+year,HttpStatus.NOT_FOUND);
        }
//        fetch all the attendance record for the given month
        List<Attendance> attendanceList = getEmployeeAttendanceBetween(employee,startDate,endDate);

//        count total days present for the month
        Long totalDaysPresent = attendanceList.stream().filter(Attendance::isPresent).count();

        log.info("findTotalDaysPresentInMonth : {} {} {}",employee,year,totalDaysPresent);

//        return the value
        return totalDaysPresent;
    }

    @Override
    @Transactional
    public void deleteEmployeeAttendanceBetween(Employee employee, LocalDate startDate, LocalDate endDate){
        attendanceRepository.deleteByEmployeeAndDateBetween(employee,startDate,endDate);
        log.info("deleteEmployeeAttendanceBetween : {} {} {}",employee,startDate,endDate);
    }
}
