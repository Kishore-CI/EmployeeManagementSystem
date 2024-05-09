package com.example.EmployeeManagementSystem.Repository;

import com.example.EmployeeManagementSystem.Model.Attendance;
import com.example.EmployeeManagementSystem.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {

    public List<Attendance> findByemployee(Employee employee);
    public Attendance findByEmployeeAndDate(Employee employee, LocalDate date);
}
