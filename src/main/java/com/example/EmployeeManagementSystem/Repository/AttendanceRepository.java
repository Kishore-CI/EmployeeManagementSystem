package com.example.EmployeeManagementSystem.Repository;

import com.example.EmployeeManagementSystem.Model.Attendance;
import com.example.EmployeeManagementSystem.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {

    public List<Attendance> findByemployee(Employee employee);

    public Attendance findByEmployeeAndDate(Employee employee, LocalDate date);

    public List<Attendance> findByEmployeeAndDateBetween(Employee employee, LocalDate startDate, LocalDate endDate);

    @Transactional
    public void deleteByEmployeeAndDateBetween(Employee employee,LocalDate startDate, LocalDate endDate);

    public void deleteByemployee(Employee employee);
}
