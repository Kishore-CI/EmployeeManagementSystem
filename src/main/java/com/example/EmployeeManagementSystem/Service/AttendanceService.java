package com.example.EmployeeManagementSystem.Service;

import com.example.EmployeeManagementSystem.Model.Attendance;
import com.example.EmployeeManagementSystem.Model.Employee;

import java.util.List;

public interface AttendanceService {

    public List<Attendance> getAttendanceForemployee(Employee employee);
}
