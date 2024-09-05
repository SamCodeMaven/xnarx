package uz.xnarx.productservice.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.xnarx.productservice.payload.ErrorDto;
import uz.xnarx.productservice.payload.enums.ErrorCode;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(400).body(ErrorDto.builder().code(ErrorCode.ENTITY_NOT_FOUND).message(e.getMessage()).build());
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorDto> handleEntityExistsException(EntityExistsException e) {
        return ResponseEntity.status(400).body(ErrorDto.builder().code(ErrorCode.ENTITY_ALREADY_EXISTS).message(e.getMessage()).build());
    }
}


