package com.example.EmployeeManagementSystem.Service;

import com.example.EmployeeManagementSystem.Model.Attendance;
import com.example.EmployeeManagementSystem.Model.Employee;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

public interface AttendanceService {

    public List<Attendance> getAttendanceForemployee(Employee employee);

    public List<Attendance> getEmployeeAttendanceBetween(Employee employee, LocalDate startDate, LocalDate endDate);

    public Attendance updateAttendanceForemployee(Long id, LocalDate date, boolean present);

    public List<Attendance> updateAttendanceForemployeeInBulk(Long id, LocalDate startDate, LocalDate endDate, boolean present);

    public Attendance findAttendanceRecord(Employee employee, LocalDate date);

    public Employee findEmployee(Long id);

    public List<Attendance> getAttendance(Long id);

    public void generateAttendanceForAll(LocalDate startDate, LocalDate endDate);

    public void generateAttendanceForEmployee(Long id,LocalDate startDate, LocalDate endDate);

    public void deleteAttendance(Long id, LocalDate date);

    public void deleteAllAttendance();

    public void deleteAllAttendanceForEmployee(Long id);

    public void saveRecord(Attendance attendanceRecord);

    public Long findTotalDaysPresentInMonth(Long id, Month month, Year year);

    public Long findTotalDaysPresentInYear(Long id, Year year);
}
