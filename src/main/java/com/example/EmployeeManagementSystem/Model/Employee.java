package com.example.EmployeeManagementSystem.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;

@Entity
@Table(name = "EMS_EMPLOYEE")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "EMPLOYEE_ID",length = 255)
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
    @Min(value = 1, message = "Employee's salary cannot be 0")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private int salary;

    @Transient
    @JsonIgnore
    private String message;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private List<Attendance> attendanceList;

    public Employee() {
    }

    public Employee(String message){
        this.message = message;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
