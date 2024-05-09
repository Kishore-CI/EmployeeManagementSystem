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
import java.util.List;

@Controller
@Validated
public class AttendanceController {

    private Logger log = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    private AttendanceService attendanceService;

    @RequestMapping(value = "api/v1/json/attendance/updateAttendance/{id}", method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updateAttendance(@Valid @PathVariable("id") Long id,
                                              @Valid @RequestParam LocalDate date,
                                              @Valid @RequestParam boolean present) throws MethodArgumentTypeMismatchException{

        log.info("updateAttendance -> Request Received : {} {} {}",id,date,present);

        Attendance attendance = attendanceService.updateAttendanceForemployee(id,date,present);

        if(attendance.getMessage() != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(attendance.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(attendance);
    }

    @RequestMapping(value = "api/v1/json/attendance/getAttendance/{id}", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getAttendance(@Valid @PathVariable("id") Long id){

        log.info("getAttendance -> Request Received : {}",id);

        List<Attendance> attendanceList = attendanceService.getAttendance(id);

        if( attendanceList.size() == 1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(attendanceList.getFirst().getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(attendanceList);
    }

    @RequestMapping(value = "api/v1/json/attendance/updateAttendanceInBulk/{id}", method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updateAttendanceInBulk(@Valid @PathVariable("id") Long id,
                                                    @Valid @RequestParam LocalDate startDate,
                                                    @Valid @RequestParam LocalDate endDate,
                                                    @Valid @RequestParam boolean present){

//        log the request data
        log.info("updateAttendanceInBulk -> Request Received : {} {} {} {}",id,startDate,endDate,present);

//        Update the attendance for the employee
        List<Attendance> attendanceList = attendanceService.updateAttendanceForemployeeInBulk(id, startDate, endDate, present);

//        Return the appropriate HTTP response
        if(attendanceList == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No employee found for the id : "+id);
        }
        else if(attendanceList.size() == 1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(attendanceList.getFirst().getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(attendanceList);
    }


}
