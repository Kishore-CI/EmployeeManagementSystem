package com.example.EmployeeManagementSystem.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="EMS_Attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "attendace_id",length = 255)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;


    @Temporal(TemporalType.DATE)
    @Column(name = "attendance_date")
    private LocalDate date;

    @Column(name = "is_present")
    private boolean present;

    @Transient
    @JsonIgnore
    private String message;


    public Attendance() {
    }

    public Attendance(String message){
        this.message = message;
    }

    public Attendance(Employee employee, LocalDate date, boolean present) {
        this.employee = employee;
        this.date = date;
        this.present = present;
    }

    public Attendance(Long id, Employee employee, LocalDate date, boolean present) {
        this.id = id;
        this.employee = employee;
        this.date = date;
        this.present = present;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", employee=" + employee +
                ", date=" + date +
                ", present=" + present +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
