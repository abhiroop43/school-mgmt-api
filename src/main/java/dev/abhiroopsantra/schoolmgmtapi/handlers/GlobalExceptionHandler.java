package dev.abhiroopsantra.schoolmgmtapi.handlers;

import dev.abhiroopsantra.schoolmgmtapi.dto.ApiResponse;
import dev.abhiroopsantra.schoolmgmtapi.exceptions.BadRequestException;
import dev.abhiroopsantra.schoolmgmtapi.exceptions.NotFoundException;
import dev.abhiroopsantra.schoolmgmtapi.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(new ApiResponse(null, "1", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class) public ResponseEntity<ApiResponse> handleException(Exception ex) {
        return new ResponseEntity<>(new ApiResponse(null, "2", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorizedException(UnauthorizedException ex) {
        return new ResponseEntity<>(new ApiResponse(null, "3", ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequestException(BadRequestException ex) {
        return new ResponseEntity<>(new ApiResponse(null, "4", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
