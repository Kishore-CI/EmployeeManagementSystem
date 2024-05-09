package com.example.EmployeeManagementSystem.Service;

import com.example.EmployeeManagementSystem.Model.Attendance;
import com.example.EmployeeManagementSystem.Model.Employee;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    public List<Attendance> getAttendanceForemployee(Employee employee);

    public Attendance updateAttendanceForemployee(Long id, LocalDate date, boolean present);

    public Employee findEmployee(Long id);

    public List<Attendance> getAttendance(Long id);
}
