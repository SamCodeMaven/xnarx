package uz.xnarx.productservice.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.xnarx.productservice.payload.ErrorDto;
import uz.xnarx.productservice.payload.ExceptionDto;

import java.util.Date;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleEntityNotFoundException(EntityNotFoundException e) {
        ExceptionDto.ErrorResult errorResult=new ExceptionDto.ErrorResult(404, new Date(), e.getMessage());
        ExceptionDto response=new ExceptionDto(errorResult);
        log.error("handleEntityNotFoundException: {}",e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ExceptionDto> handleEntityExistsException(EntityExistsException e) {
        ExceptionDto.ErrorResult errorResult=new ExceptionDto.ErrorResult(409, new Date(), e.getMessage());
        ExceptionDto response=new ExceptionDto(errorResult);
        log.error(": {}",e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDto> handleCustomException(CustomException ex) {
        log.error("ErrorStatus: {}, Message: {} ", ex.getCode(), ex.getMessage());

        return ResponseEntity
                .status(ex.getCode())
                .body(new ErrorDto(ex.getCode(), ex.getMessage()));
    }

}


