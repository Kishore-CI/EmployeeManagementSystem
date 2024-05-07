package com.example.EmployeeManagementSystem.Model;

import jakarta.persistence.*;
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
    private Date date;

    private boolean present;
}
