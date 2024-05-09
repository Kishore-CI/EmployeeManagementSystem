package com.example.EmployeeManagementSystem.Service.Implementation;

import com.example.EmployeeManagementSystem.Model.Attendance;
import com.example.EmployeeManagementSystem.Model.Employee;
import com.example.EmployeeManagementSystem.Repository.AttendanceRepository;
import com.example.EmployeeManagementSystem.Service.AttendanceService;
import com.example.EmployeeManagementSystem.Service.EmployeeService;
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private Logger log = LoggerFactory.getLogger(AttendanceService.class);

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public List<Attendance> getAttendanceForemployee(Employee employee) {
//        find the attendanceList for the employee
        List<Attendance> attendanceList = attendanceRepository.findByemployee(employee);
        return attendanceList;
    }

    @Override
    public Attendance updateAttendanceForemployee(Long id, LocalDate date, boolean present) {
        Employee employee = findEmployee(id);
        if(employee == null){
            return new Attendance("No employee found for id : "+id);
        }
        Attendance attendanceRecord = attendanceRepository.findByEmployeeAndDate(employee, date);
        if(attendanceRecord == null){
            log.info("No attendance record found for employee id : {} on date : {}",id,date);
            return new Attendance("No attendance record found for employee id : "+id+" on date : "+date);
        }
        attendanceRecord.setPresent(present);
        return attendanceRepository.save(attendanceRecord);
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
            return List.of(new Attendance("No Employee found for id : "+id));
        }
        return getAttendanceForemployee(employee);
    }
}
