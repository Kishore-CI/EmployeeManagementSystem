package com.example.EmployeeManagementSystem.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "EMS_EARNED_SALARY")
public class EarnedSalary {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Double earned_salary;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;


    public EarnedSalary(Long id, Double earned_salary, Employee employee) {
        this.id = id;
        this.earned_salary = earned_salary;
        this.employee = employee;
    }

    public EarnedSalary(Double earned_salary, Employee employee) {
        this.earned_salary = earned_salary;
        this.employee = employee;
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

    @Override
    public String toString() {
        return "EarnedSalary{" +
                "id=" + id +
                ", earned_salary=" + earned_salary +
                ", employee=" + employee +
                '}';
    }
}
