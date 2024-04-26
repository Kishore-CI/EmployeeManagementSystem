package com.example.EmployeeManagementSystem.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "EMPLOYEE_ID")
    private long id;

    @Column(name="NAME")
    @NotNull(message = "Employee's Name cannot be null")
    private String name;

    @Column(name = "EMAIL")
    @NotNull(message = "Employee's Email cannot be null")
    @Email
    private String email;

    @Column(name = "DEPARTMENT")
    @NotNull(message = "Employee's Department cannot be null")
    private String department;

    @Column(name = "POSITION")
    @NotNull(message = "Employee's Position cannot be null")
    private String position;

    @Column(name = "SALARY")
    @Min(1)
    private int salary;

    public Employee() {
    }

    public Employee(long id, String name, String email, String department, String position, int salary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.position = position;
        this.salary = salary;
    }

    public Employee(String name, String email, String department, String position, int salary) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.position = position;
        this.salary = salary;
    }


}
