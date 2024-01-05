package dev.abhiroopsantra.schoolmgmtapi.controllers;

import dev.abhiroopsantra.schoolmgmtapi.dto.ApiResponse;
import dev.abhiroopsantra.schoolmgmtapi.dto.FeeDto;
import dev.abhiroopsantra.schoolmgmtapi.dto.StudentLeaveDto;
import dev.abhiroopsantra.schoolmgmtapi.dto.UserDto;
import dev.abhiroopsantra.schoolmgmtapi.enums.StudentLeaveStatus;
import dev.abhiroopsantra.schoolmgmtapi.exceptions.BadRequestException;
import dev.abhiroopsantra.schoolmgmtapi.exceptions.NotFoundException;
import dev.abhiroopsantra.schoolmgmtapi.services.admin.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController @RequestMapping("/api/admin") @PreAuthorize("hasAuthority('ADMIN')") public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // api method to create a new student
    @PostMapping("/students") public ResponseEntity<ApiResponse> addStudent(@RequestBody UserDto studentDto) {
        UserDto createdStudent = adminService.postStudent(studentDto);
        if (createdStudent == null) {
            throw new BadRequestException("Unable to create student");
        }

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("student", createdStudent);
        return new ResponseEntity<>(
                new ApiResponse(responseData, "0", "Student created successfully"), HttpStatus.CREATED);

    }

    // api method to list all students
    @GetMapping("/students") public ResponseEntity<ApiResponse> getStudentsList(Pageable pageable) {

        // TODO: Add filtering

        Page<UserDto>           students     = adminService.getStudents(pageable);
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("students", students.getContent());
        responseData.put("currentPage", students.getNumber());
        responseData.put("totalItems", students.getTotalElements());
        responseData.put("totalPages", students.getTotalPages());

        return new ResponseEntity<>(new ApiResponse(responseData, "0", "Students fetched successfully"), HttpStatus.OK);
    }

    // api method to get a specific student by id
    @GetMapping("/students/{id}") public ResponseEntity<ApiResponse> getStudentById(@PathVariable("id") Long id) {

        UserDto student = adminService.getStudentById(id);

        if (student == null) {
            throw new NotFoundException("Unable to find student with id " + id);
        }

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("student", student);

        return new ResponseEntity<>(new ApiResponse(responseData, "0", "Student fetched successfully"), HttpStatus.OK);
    }

    // api method to update a student
    @PutMapping("/students/{id}") public ResponseEntity<ApiResponse> updateStudent(
            @PathVariable("id") Long id, @RequestBody UserDto studentDto
                                                                                  ) {
        UserDto updatedStudent = adminService.updateStudent(id, studentDto);

        if (updatedStudent == null) {
            throw new BadRequestException("Unable to update student");
        }

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("student", updatedStudent);

        return new ResponseEntity<>(new ApiResponse(responseData, "0", "Student updated successfully"), HttpStatus.OK);
    }

    // api to delete a student
    @DeleteMapping("/students/{id}") public ResponseEntity<ApiResponse> deleteStudent(@PathVariable("id") Long id) {
        Boolean isDeleted = adminService.deleteStudent(id);

        if (!isDeleted) {
            throw new BadRequestException("Unable to delete student");
        }

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("isDeleted", isDeleted);

        return new ResponseEntity<>(new ApiResponse(responseData, "0", "Student deleted successfully"), HttpStatus.OK);
    }


    // pay the fees for a student
    @PostMapping("/fee/{studentId}") public ResponseEntity<ApiResponse> payFee(
            @PathVariable Long studentId, @RequestBody FeeDto feeDto
                                                                              ) {
        FeeDto paidFee = adminService.payFee(studentId, feeDto);

        if (paidFee == null) {
            throw new BadRequestException("Unable to pay fee for the student");
        }

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("fee", paidFee);

        return new ResponseEntity<>(new ApiResponse(responseData, "0", "Fee paid successfully"), HttpStatus.CREATED);

    }

    // list all students leaves
    @GetMapping("/students/leaves") public ResponseEntity<ApiResponse> getAllAppliedLeavesByStudentId(
            Pageable pageable
                                                                                                     ) {
        // TODO: Add pagination and filtering

        Page<StudentLeaveDto> leaves = adminService.getAllAppliedLeaves(pageable);

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("students", leaves.getContent());
        responseData.put("currentPage", leaves.getNumber());
        responseData.put("totalItems", leaves.getTotalElements());
        responseData.put("totalPages", leaves.getTotalPages());

        return new ResponseEntity<>(new ApiResponse(responseData, "0", "Student fetched successfully"), HttpStatus.OK);
    }

    // update student leave status
    @PutMapping("/students/leaves/{leaveId}") public ResponseEntity<ApiResponse> updateStudentLeaveStatus(
            @PathVariable Long leaveId, @RequestBody StudentLeaveStatus leaveStatus
                                                                                                         ) {
        boolean isUpdateSuccessful = adminService.updateStudentLeaveStatus(leaveId, leaveStatus);
        if (!isUpdateSuccessful) {
            throw new BadRequestException("Unable to update leave status");
        }
        return new ResponseEntity<>(new ApiResponse(null, "0", "Leave status updated successfully"), HttpStatus.OK);
    }
}
