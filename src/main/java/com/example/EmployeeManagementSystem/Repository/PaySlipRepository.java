package com.example.EmployeeManagementSystem.Repository;

import com.example.EmployeeManagementSystem.Model.PaySlip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaySlipRepository extends JpaRepository<PaySlip,Long> {
}
