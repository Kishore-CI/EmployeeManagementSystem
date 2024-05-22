package com.example.EmployeeManagementSystem.Model;

import jakarta.persistence.*;

import java.time.Month;
import java.time.Year;

@Entity
@Table(name = "ems_payslip")
public class PaySlip {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "payslip_id",length = 255)
    private Long id;

    @Column(name = "month")
    private Month month;

    @Column(name = "year")
    private Year year;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Lob
    @Column(nullable = false)
    private byte[] payslipdata;

    public PaySlip() {
    }

    public PaySlip(Long id, Month month, Year year, Employee employee, byte[] payslipdata) {
        this.id = id;
        this.month = month;
        this.year = year;
        this.employee = employee;
        this.payslipdata = payslipdata;
    }

    public PaySlip(Month month, Year year, Employee employee, byte[] payslipdata) {
        this.month = month;
        this.year = year;
        this.employee = employee;
        this.payslipdata = payslipdata;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public byte[] getPayslipdata() {
        return payslipdata;
    }

    public void setPayslipdata(byte[] payslipdata) {
        this.payslipdata = payslipdata;
    }

    @Override
    public String toString() {
        return "PaySlip{" +
                "id=" + id +
                ", month=" + month +
                ", year=" + year +
                ", employee=" + employee +
                '}';
    }
}
