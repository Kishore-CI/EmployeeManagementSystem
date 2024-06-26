package com.example.EmployeeManagementSystem.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
    @Valid
    private String name;

    @Column(name = "EMAIL")
    @NotNull(message = "Employee's Email cannot be null")
    @Email
    @Valid
    private String email;

    @Column(name = "PHONE_NUMBER")
    @NotNull(message = "Employee's Phone number cannot be null")
    @Min(value = 1000000000L, message = "Phone number is below valid length")
    @Max(value = 9999999999L, message = "Phone number is beyond valid length")
    @Valid
    private long phone;

    @Column(name = "DEPARTMENT")
    @NotNull(message = "Employee's Department cannot be null")
    @Valid
    private String department;

    @Column(name = "POSITION")
    @NotNull(message = "Employee's Position cannot be null")
    @Valid
    private String position;

    @Column(name = "SALARY")
    @Min(value = 1, message = "Employee's salary cannot be 0")
    @NotNull(message = "Employee's salary cannot be null")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Valid
    private int salary;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee",orphanRemoval = true)
    private List<Attendance> attendanceList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee",orphanRemoval = true)
    private List<EarnedSalary> earned_salary;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee",orphanRemoval = true)
    private List<PaySlip> paySlip;

    public Employee() {
    }


    public Employee(long id, String name, String email, Long phone, String department, String position, int salary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.position = position;
        this.salary = salary;
    }

    public Employee(String name, String email, Long phone, String department, String position, int salary) {
        this.name = name;
        this.email = email;
        this.phone = phone;
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

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }
}
