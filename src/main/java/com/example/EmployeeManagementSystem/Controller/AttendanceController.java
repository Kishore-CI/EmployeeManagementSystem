package com.example.EmployeeManagementSystem.Controller;

import com.example.EmployeeManagementSystem.Model.Attendance;
import com.example.EmployeeManagementSystem.Service.AttendanceService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

@Controller
@Validated
public class AttendanceController {

    private Logger log = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    private AttendanceService attendanceService;

    @RequestMapping(value = "api/v1/json/attendance/updateAttendance/", method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updateAttendance(@Valid @RequestParam Long id,
                                              @Valid @RequestParam LocalDate date,
                                              @Valid @RequestParam boolean present) throws MethodArgumentTypeMismatchException{

        log.info("updateAttendance -> Request Received : {} {} {}",id,date,present);

        Attendance attendance = attendanceService.updateAttendanceForemployee(id,date,present);

        return ResponseEntity.status(HttpStatus.OK).body(attendance);
    }

    @RequestMapping(value = "api/v1/json/attendance/getAttendance/", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getAttendance(@Valid @RequestParam Long id) throws MethodArgumentTypeMismatchException{

        log.info("getAttendance -> Request Received : {}",id);

        List<Attendance> attendanceList = attendanceService.getAttendance(id);

        return ResponseEntity.status(HttpStatus.OK).body(attendanceList);
    }

    @RequestMapping(value = "api/v1/json/attendance/updateAttendanceInBulk/", method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updateAttendanceInBulk(@Valid @RequestParam Long id,
                                                    @Valid @RequestParam LocalDate startDate,
                                                    @Valid @RequestParam LocalDate endDate,
                                                    @Valid @RequestParam boolean present) throws MethodArgumentTypeMismatchException{

//        log the request data
        log.info("updateAttendanceInBulk -> Request Received : {} {} {} {}",id,startDate,endDate,present);

//        Update the attendance for the employee
        List<Attendance> attendanceList = attendanceService.updateAttendanceForemployeeInBulk(id, startDate, endDate, present);

//        Return the appropriate HTTP response

        return ResponseEntity.status(HttpStatus.OK).body(attendanceList);
    }

    @RequestMapping(value = "api/v1/json/attendance/generateAttendanceForAll", method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> generateAttendance(@Valid @RequestParam LocalDate startDate,
                                                @Valid @RequestParam LocalDate endDate){
//        log the request
        log.info("generateAttendance : Request Received");
//        generate the attendance
        List<Attendance> attendanceList = attendanceService.generateAttendanceForAll(startDate,endDate);
//        return the response entity
        return ResponseEntity.status(HttpStatus.OK).body(attendanceList);
    }

    @RequestMapping(value = "api/v1/json/attendance/generateAttendanceForEmployee/", method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> generateAttendanceForEmployee(@Valid @RequestParam Long employeeIdMobile,
                                                           @Valid @RequestParam LocalDate startDate,
                                                           @Valid @RequestParam LocalDate endDate){
//        log the request
        log.info("generateAttendanceForEmployee : Request Received : {}",employeeIdMobile.getClass());
//        generate attendance only for the specific employee
        List<Attendance> attendanceList = attendanceService.generateAttendanceForEmployee(employeeIdMobile,startDate,endDate);
//        return the response
        return ResponseEntity.status(HttpStatus.OK).body(attendanceList);
    }

    @RequestMapping(value = "api/v1/json/attendance/deleteAttendanceRecord/", method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> deleteAttendanceRecord(@Valid @RequestParam Long id,
                                                    @Valid @RequestParam LocalDate date){
//        log the request
        log.info("deleteAttendanceRecord : Request Received: {} {}", id,date);

//        delete the attendance record for the id and date
        attendanceService.deleteAttendance(id,date);

//        return appropriate HTTP response
        return ResponseEntity.status(HttpStatus.OK).body("Attendance record for employee id: "+id+" for date: "+date+" has been deleted");
    }

    @RequestMapping(value = "api/v1/json/attendance/deleteAllAttendanceForEmployee/", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> deleteAllAttendanceForEmployee(@Valid @RequestParam Long id){
//        log the request
        log.info("deleteAllAttendanceForEmployee : Request Received: {}", id);

//        delete all attendance record for that employee
        attendanceService.deleteAllAttendanceForEmployee(id);

//        return appropriate HTTP response
        return ResponseEntity.status(HttpStatus.OK).body("All Attendance records for employee id: "+id+" has been deleted");
    }

    @RequestMapping(value = "api/v1/json/attendance/getTotalDaysPresentInMonth/", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getTotalDaysPresentMonth(@Valid @RequestParam Long id,
                                                      @Valid @RequestParam Month month,
                                                      @Valid @RequestParam Year year){
//        log the request
        log.info("getTotalDaysPresentMonth : request received");

//        find the total days present for the month
        Long daysPresent = attendanceService.findTotalDaysPresentInMonth(id,month, year);

//        return appropriate HTTP response
        return ResponseEntity.status(HttpStatus.OK).body(daysPresent);
    }

    @RequestMapping(value = "api/v1/json/attendance/getTotalDaysPresentInYear/", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getTotalDaysPresentYear(@Valid @RequestParam Long id,
                                                      @Valid @RequestParam Year year ){
//        log the request
        log.info("getTotalDaysPresentMonth : request received");

//        find the total days present for the month
        Long daysPresent = attendanceService.findTotalDaysPresentInYear(id,year);

//        return appropriate HTTP response
        return ResponseEntity.status(HttpStatus.OK).body(daysPresent);
    }

}
