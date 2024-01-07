package dev.abhiroopsantra.schoolmgmtapi.controllers;

import dev.abhiroopsantra.schoolmgmtapi.dto.*;
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

    // api method to create new teacher
    @PostMapping("/teachers") public ResponseEntity<ApiResponse> addTeacher(@RequestBody TeacherDto teacherDto) {
        TeacherDto createdTeacher = adminService.postTeacher(teacherDto);
        if (createdTeacher == null) {
            throw new BadRequestException("Unable to create teacher");
        }

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("teacher", createdTeacher);
        return new ResponseEntity<>(
                new ApiResponse(responseData, "0", "Teacher created successfully"), HttpStatus.CREATED);

    }

    // api method to get list of teachers
    @GetMapping("/teachers") public ResponseEntity<ApiResponse> getTeachersList(Pageable pageable) {
        Page<TeacherDto>        teachers     = adminService.getTeachers(pageable);
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("teachers", teachers.getContent());
        responseData.put("currentPage", teachers.getNumber());
        responseData.put("totalItems", teachers.getTotalElements());
        responseData.put("totalPages", teachers.getTotalPages());

        return new ResponseEntity<>(new ApiResponse(responseData, "0", "Teachers fetched successfully"), HttpStatus.OK);
    }

    // api method to get a specific teacher by id
    @GetMapping("/teachers/{id}") public ResponseEntity<ApiResponse> getTeacherById(@PathVariable("id") Long id) {

        TeacherDto teacher = adminService.getTeacherById(id);

        if (teacher == null) {
            throw new NotFoundException("Unable to find teacher with id " + id);
        }

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("teacher", teacher);

        return new ResponseEntity<>(new ApiResponse(responseData, "0", "Teacher fetched successfully"), HttpStatus.OK);
    }

    // api method to update a teacher
    @PutMapping("/teachers/{id}") public ResponseEntity<ApiResponse> updateTeacher(
            @PathVariable("id") Long id, @RequestBody TeacherDto teacherDto
                                                                                  ) {
        TeacherDto updatedTeacher = adminService.updateTeacher(id, teacherDto);

        if (updatedTeacher == null) {
            throw new BadRequestException("Unable to update teacher");
        }

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("teacher", updatedTeacher);

        return new ResponseEntity<>(new ApiResponse(responseData, "0", "Teacher updated successfully"), HttpStatus.OK);
    }

    // api method to delete a teacher by id
    @DeleteMapping("/teachers/{id}") public ResponseEntity<ApiResponse> deleteTeacher(@PathVariable("id") Long id) {
        Boolean isDeleted = adminService.deleteTeacher(id);

        if (!isDeleted) {
            throw new BadRequestException("Unable to delete teacher");
        }

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("isDeleted", true);

        return new ResponseEntity<>(new ApiResponse(responseData, "0", "Teacher deleted successfully"), HttpStatus.OK);
    }
}
