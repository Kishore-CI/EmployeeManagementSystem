package com.example.EmployeeManagementSystem.Service;

import com.example.EmployeeManagementSystem.Model.Attendance;
import com.example.EmployeeManagementSystem.Model.Employee;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public interface AttendanceService {

    public List<Attendance> getAttendanceForemployee(Employee employee);

    public List<Attendance> getEmployeeAttendanceBetween(Employee employee, LocalDate startDate, LocalDate endDate);

    public Attendance updateAttendanceForemployee(Long id, LocalDate date, boolean present);

    public List<Attendance> updateAttendanceForemployeeInBulk(Long id, LocalDate startDate, LocalDate endDate, boolean present);

    public Attendance findAttendanceRecord(Employee employee, LocalDate date);

    public Employee findEmployee(Long id);

    public List<Attendance> getAttendance(Long id);

    public void generateAttendanceForAll();

    public void generateAttendanceForEmployee(Long id);

    public void deleteAttendance(Long id, LocalDate date);

    public void deleteAllAttendance();

    public void deleteAllAttendanceForEmployee(Long id);

    public void saveRecord(Attendance attendanceRecord);
}
