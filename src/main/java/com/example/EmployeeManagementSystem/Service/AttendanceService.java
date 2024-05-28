package com.example.EmployeeManagementSystem.Service;

import com.example.EmployeeManagementSystem.Model.Attendance;
import com.example.EmployeeManagementSystem.Model.Employee;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

public interface AttendanceService {

    /**
     * Utility method that gets all the attendance records for an employee
     * @param employee
     * @return List<Attendance>
     */
    public List<Attendance> getAttendanceForemployee(Employee employee);

    /**
     * Utility method that gets the attendance records for an employee between give dates
     * @param employee
     * @param startDate
     * @param endDate
     * @return List<Attendance>
     */
    public List<Attendance> getEmployeeAttendanceBetween(Employee employee, LocalDate startDate, LocalDate endDate);

    /**
     * Updates the attendance for an employee on a given date
     * @param id
     * @param date
     * @param present
     * @return Updated attendance record
     */
    public Attendance updateAttendanceForemployee(Long id, LocalDate date, boolean present);

    /**
     * Updates the attendance for an employee between given date range with the given 'present' value
     * @param id
     * @param startDate
     * @param endDate
     * @param present
     * @return List<Attendance>
     */
    public List<Attendance> updateAttendanceForemployeeInBulk(Long id, LocalDate startDate, LocalDate endDate, boolean present);

    /**
     * Finds the attendance record for an employee for a given date
     * @param employee
     * @param date
     * @return Attendance
     */
    public Attendance findAttendanceRecord(Employee employee, LocalDate date);

    /**
     * Finds the attendance for an employee of the given id
     * @param id
     * @return List<Attendance>
     */
    public List<Attendance> getAttendance(Long id);

    /**
     * Gets the attendance records for all the employees, between the give dates. Used by the controller
     * @param startDate
     * @param endDate
     * @return List<Attendance>
     */
    public List<Attendance> generateAttendanceForAll(LocalDate startDate, LocalDate endDate);

    /**
     * Gets the attendace for an employee between the given dates. Used by the controller
     * @param id
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Attendance> generateAttendanceForEmployee(Long id,LocalDate startDate, LocalDate endDate);

    /**
     * Deletes the attendance record for an employee at a particular date
     * @param id
     * @param date
     */
    public void deleteAttendance(Long id, LocalDate date);

    /**
     * Deletes all the attendance records in the database
     */
    public void deleteAllAttendance();

    /**
     * Deletes all the attendance for an employee with the given id
     * @param id
     */
    public void deleteAllAttendanceForEmployee(Long id);

    /**
     * Saves an attendance record to the database
     * @param attendanceRecord
     */
    public void saveRecord(Attendance attendanceRecord);

    /**
     * Finds the total days for which the employee was present for the given month
     * @param id
     * @param month
     * @param year
     * @return Total days present
     */
    public Long findTotalDaysPresentInMonth(Long id, Month month, Year year);

    /**
     * Finds the total days for which the employee was present in the year overall
     * @param id
     * @param year
     * @return
     */
    public Long findTotalDaysPresentInYear(Long id, Year year);

    /**
     * Deletes the attendance records for an employee between the given date range
     * @param employee
     * @param startDate
     * @param endDate
     */
    @Transactional
    void deleteEmployeeAttendanceBetween(Employee employee, LocalDate startDate, LocalDate endDate);
}
