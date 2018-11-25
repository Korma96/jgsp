package com.mjvs.jgsp.helpers.exception;

import com.mjvs.jgsp.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionResolver
{
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity databaseException(DatabaseException exception)
    {
        return new ResponseEntity<>(new ErrorDTO(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity badRequestException(BadRequestException exception)
    {
        return new ResponseEntity<>(new ErrorDTO(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }


}
