package com.example.EmployeeManagementSystem.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Month;

@Entity
@Table(name = "EMS_EARNED_SALARY")
public class EarnedSalary {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Double earned_salary;

    private Month month;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;


    public EarnedSalary(Long id, Double earned_salary, Employee employee, Month month) {
        this.id = id;
        this.earned_salary = earned_salary;
        this.employee = employee;
        this.month = month;
    }

    public EarnedSalary(Double earned_salary, Employee employee, Month month) {
        this.earned_salary = earned_salary;
        this.employee = employee;
        this.month = month;
    }

    public EarnedSalary() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getEarned_salary() {
        return earned_salary;
    }

    public void setEarned_salary(Double earned_salary) {
        this.earned_salary = earned_salary;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "EarnedSalary{" +
                "id=" + id +
                ", earned_salary=" + earned_salary +
                ", employee=" + employee +
                '}';
    }
}
