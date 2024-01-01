package dev.abhiroopsantra.schoolmgmtapi.controllers;

import dev.abhiroopsantra.schoolmgmtapi.dto.ApiResponse;
import dev.abhiroopsantra.schoolmgmtapi.dto.UserDto;
import dev.abhiroopsantra.schoolmgmtapi.exceptions.BadRequestException;
import dev.abhiroopsantra.schoolmgmtapi.exceptions.NotFoundException;
import dev.abhiroopsantra.schoolmgmtapi.exceptions.UnauthorizedException;
import dev.abhiroopsantra.schoolmgmtapi.services.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController @RequiredArgsConstructor @RequestMapping("/api/student") public class StudentController {
    private final StudentService studentService;

    // api method to get a specific student by id
    @GetMapping("/{id}") public ResponseEntity<ApiResponse> getStudentById(@PathVariable("id") Long id) {
        try {
            UserDto student = studentService.getStudentById(id);

            HashMap<String, Object> responseData = new HashMap<>();
            responseData.put("student", student);

            return new ResponseEntity<>(
                    new ApiResponse(responseData, "0", "Student fetched successfully"), HttpStatus.OK);
        }
        catch (UnauthorizedException ex) {
            return new ResponseEntity<>(new ApiResponse(null, "3", ex.getMessage()), HttpStatus.FORBIDDEN);
        }
        catch (NotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(null, "1", ex.getMessage()), HttpStatus.NOT_FOUND);
        }
        catch (BadRequestException ex) {
            return new ResponseEntity<>(new ApiResponse(null, "4", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse(null, "2", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
