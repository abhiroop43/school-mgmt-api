package dev.abhiroopsantra.schoolmgmtapi.handlers;

import dev.abhiroopsantra.schoolmgmtapi.dto.ApiResponse;
import dev.abhiroopsantra.schoolmgmtapi.exceptions.BadRequestException;
import dev.abhiroopsantra.schoolmgmtapi.exceptions.NotFoundException;
import dev.abhiroopsantra.schoolmgmtapi.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestControllerAdvice public class GlobalExceptionHandler {

    Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFoundException(NotFoundException ex) {
        logger.log(Level.WARNING, "No Data Found", ex);
        return new ResponseEntity<>(new ApiResponse(null, "1", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class) public ResponseEntity<ApiResponse> handleException(Exception ex) {
        logger.log(Level.SEVERE, "Internal Server Error", ex);
        return new ResponseEntity<>(new ApiResponse(null, "2", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorizedException(UnauthorizedException ex) {
        logger.log(Level.WARNING, "Unauthorized Access to API", ex);
        return new ResponseEntity<>(new ApiResponse(null, "3", ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequestException(BadRequestException ex) {
        logger.log(Level.SEVERE, "Request was invalid", ex);
        return new ResponseEntity<>(new ApiResponse(null, "4", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
