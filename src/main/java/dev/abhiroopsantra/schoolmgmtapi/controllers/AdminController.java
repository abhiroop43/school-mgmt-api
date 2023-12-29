package dev.abhiroopsantra.schoolmgmtapi.controllers;

import dev.abhiroopsantra.schoolmgmtapi.dto.ApiResponse;
import dev.abhiroopsantra.schoolmgmtapi.dto.UserDto;
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
}
