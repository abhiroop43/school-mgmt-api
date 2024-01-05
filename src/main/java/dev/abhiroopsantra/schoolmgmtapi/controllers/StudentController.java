package dev.abhiroopsantra.schoolmgmtapi.controllers;

import dev.abhiroopsantra.schoolmgmtapi.dto.ApiResponse;
import dev.abhiroopsantra.schoolmgmtapi.dto.StudentLeaveDto;
import dev.abhiroopsantra.schoolmgmtapi.dto.UserDto;
import dev.abhiroopsantra.schoolmgmtapi.services.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping("/api/student") public class StudentController {
    private final StudentService studentService;

    // api method to get a specific student by id
    @GetMapping("/{id}") public ResponseEntity<ApiResponse> getStudentById(@PathVariable("id") Long id) {
        UserDto student = studentService.getStudentById(id);

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("student", student);

        return new ResponseEntity<>(new ApiResponse(responseData, "0", "Student fetched successfully"), HttpStatus.OK);
    }

    // api for student to apply for leave
    @PostMapping("/leave") public ResponseEntity<ApiResponse> applyForLeave(
            @RequestBody StudentLeaveDto studentLeaveDto
                                                                           ) {
        StudentLeaveDto student = studentService.applyForLeave(studentLeaveDto);

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("student", student);

        return new ResponseEntity<>(new ApiResponse(responseData, "0", "Leaves applied successfully"), HttpStatus.OK);
    }

    // api to get all leaves applied by student
    @GetMapping("/{id}/leaves") public ResponseEntity<ApiResponse> getAllAppliedLeavesByStudentId(
            @PathVariable("id") Long id
                                                                                                 ) {
        // TODO: Add pagination and filtering

        List<StudentLeaveDto>   leaves       = studentService.getAllAppliedLeavesByStudentId(id);
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("leaves", leaves);
        return new ResponseEntity<>(new ApiResponse(responseData, "0", "Student fetched successfully"), HttpStatus.OK);
    }

}
