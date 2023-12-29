package dev.abhiroopsantra.schoolmgmtapi.controllers;

import dev.abhiroopsantra.schoolmgmtapi.dto.ApiResponse;
import dev.abhiroopsantra.schoolmgmtapi.dto.UserDto;
import dev.abhiroopsantra.schoolmgmtapi.services.admin.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController @RequestMapping("/api/admin") public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/student") public ResponseEntity<ApiResponse> addStudent(@RequestBody UserDto studentDto) {
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
}
