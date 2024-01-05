package dev.abhiroopsantra.schoolmgmtapi.controllers;

import dev.abhiroopsantra.schoolmgmtapi.dto.ApiResponse;
import dev.abhiroopsantra.schoolmgmtapi.dto.FeeDto;
import dev.abhiroopsantra.schoolmgmtapi.dto.UserDto;
import dev.abhiroopsantra.schoolmgmtapi.enums.StudentLeaveStatus;
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
        try {
            UserDto createdStudent = adminService.postStudent(studentDto);

            if (createdStudent == null) {
                return new ResponseEntity<>(
                        new ApiResponse(null, "1", "Unable to create new student"), HttpStatus.BAD_REQUEST);
            }

            HashMap<String, Object> responseData = new HashMap<>();
            responseData.put("student", createdStudent);

            return new ResponseEntity<>(
                    new ApiResponse(responseData, "0", "Student created successfully"), HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(null, "2", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // api method to list all students
    @GetMapping("/students") public ResponseEntity<ApiResponse> getStudentsList(Pageable pageable) {
        try {
            // TODO: Add filtering

            Page<UserDto>           students     = adminService.getStudents(pageable);
            HashMap<String, Object> responseData = new HashMap<>();
            responseData.put("students", students.getContent());
            responseData.put("currentPage", students.getNumber());
            responseData.put("totalItems", students.getTotalElements());
            responseData.put("totalPages", students.getTotalPages());

            return new ResponseEntity<>(
                    new ApiResponse(responseData, "0", "Students fetched successfully"), HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(null, "2", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // api method to get a specific student by id
    @GetMapping("/students/{id}") public ResponseEntity<ApiResponse> getStudentById(@PathVariable("id") Long id) {
        try {
            UserDto student = adminService.getStudentById(id);

            if (student == null) {
                return new ResponseEntity<>(new ApiResponse(null, "1", "Unable to find student with id " + id),
                                            HttpStatus.NOT_FOUND
                );
            }

            HashMap<String, Object> responseData = new HashMap<>();
            responseData.put("student", student);

            return new ResponseEntity<>(
                    new ApiResponse(responseData, "0", "Student fetched successfully"), HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(null, "2", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // api method to update a student
    @PutMapping("/students/{id}") public ResponseEntity<ApiResponse> updateStudent(
            @PathVariable("id") Long id, @RequestBody UserDto studentDto
                                                                                  ) {
        try {
            UserDto updatedStudent = adminService.updateStudent(id, studentDto);

            if (updatedStudent == null) {
                return new ResponseEntity<>(
                        new ApiResponse(null, "1", "Unable to update student"), HttpStatus.BAD_REQUEST);
            }

            HashMap<String, Object> responseData = new HashMap<>();
            responseData.put("student", updatedStudent);

            return new ResponseEntity<>(
                    new ApiResponse(responseData, "0", "Student updated successfully"), HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(null, "2", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // api to delete a student
    @DeleteMapping("/students/{id}") public ResponseEntity<ApiResponse> deleteStudent(@PathVariable("id") Long id) {
        try {
            Boolean isDeleted = adminService.deleteStudent(id);

            if (!isDeleted) {
                return new ResponseEntity<>(
                        new ApiResponse(null, "1", "Unable to delete student"), HttpStatus.BAD_REQUEST);
            }

            HashMap<String, Object> responseData = new HashMap<>();
            responseData.put("isDeleted", isDeleted);

            return new ResponseEntity<>(
                    new ApiResponse(responseData, "0", "Student deleted successfully"), HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(null, "2", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // pay the fees for a student
    @PostMapping("/fee/{studentId}") public ResponseEntity<ApiResponse> payFee(
            @PathVariable Long studentId, @RequestBody FeeDto feeDto
                                                                              ) {
        try {
            FeeDto paidFee = adminService.payFee(studentId, feeDto);

            if (paidFee == null) {
                return new ResponseEntity<>(
                        new ApiResponse(null, "1", "Unable to pay fee for the student"), HttpStatus.BAD_REQUEST);
            }

            HashMap<String, Object> responseData = new HashMap<>();
            responseData.put("fee", paidFee);

            return new ResponseEntity<>(
                    new ApiResponse(responseData, "0", "Fee paid successfully"), HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(null, "2", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // list student leaves

    // update student leave status
    @PutMapping("/leave/{leaveId}") public ResponseEntity<ApiResponse> updateStudentLeaveStatus(
            @PathVariable Long leaveId, @RequestBody StudentLeaveStatus leaveStatus
                                                                                               ) {
        try {
            boolean isUpdateSuccessful = adminService.updateStudentLeaveStatus(leaveId, leaveStatus);

            if (!isUpdateSuccessful) {
                return new ResponseEntity<>(new ApiResponse(null, "1", "Unable to update leave status for the student"),
                                            HttpStatus.BAD_REQUEST
                );
            }

            return new ResponseEntity<>(new ApiResponse(null, "0", "Leave status updated successfully"), HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(null, "2", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
