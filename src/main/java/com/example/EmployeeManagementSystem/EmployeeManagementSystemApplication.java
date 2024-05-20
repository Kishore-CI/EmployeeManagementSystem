package com.example.EmployeeManagementSystem;


import com.example.EmployeeManagementSystem.Repository.AttendanceRepository;
import com.example.EmployeeManagementSystem.Repository.EarnedSalaryRepository;
import com.example.EmployeeManagementSystem.Thread.AttendanceThreadStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class EmployeeManagementSystemApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(EmployeeManagementSystemApplication.class, args);

//		AttendanceRepository attendanceRepository = applicationContext.getBean(AttendanceRepository.class);
//		attendanceRepository.deleteAll();
//
//		EarnedSalaryRepository earnedSalaryRepository = applicationContext.getBean(EarnedSalaryRepository.class);
//		earnedSalaryRepository.deleteAllInBatch();
//
//		AttendanceThreadStarter attendanceThreadStarter = applicationContext.getBean(AttendanceThreadStarter.class);
//		attendanceThreadStarter.startThread();


	}

}
