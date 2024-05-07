package com.example.EmployeeManagementSystem.Repository;

import com.example.EmployeeManagementSystem.Model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
}
