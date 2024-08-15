package uz.xnarx.productservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String message){super(message);}
}
