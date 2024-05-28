package com.example.EmployeeManagementSystem.Repository;

import com.example.EmployeeManagementSystem.Model.Attendance;
import com.example.EmployeeManagementSystem.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    /**
     * Finds all the attendance records for one employee
     * @param employee
     * @return
     */
    public List<Attendance> findByemployee(Employee employee);

    /**
     * Finds the attendance record for an employee on a specific date
     * @param employee
     * @param date
     * @return
     */
    public Attendance findByEmployeeAndDate(Employee employee, LocalDate date);

    /**
     * Finds all the attendance records for an employee between given time range
     * @param employee
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Attendance> findByEmployeeAndDateBetween(Employee employee, LocalDate startDate, LocalDate endDate);

    /**
     * Deletes the attendance records for an employee between given time range
     * @param employee
     * @param startDate
     * @param endDate
     */
    @Transactional
    public void deleteByEmployeeAndDateBetween(Employee employee,LocalDate startDate, LocalDate endDate);

    /**
     * Deletes all the attendance records for an employee
     * @param employee
     */
    public void deleteByemployee(Employee employee);
}
