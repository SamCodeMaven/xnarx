package uz.xnarx.productservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import uz.xnarx.productservice.payload.enums.ErrorCode;

/**
 * @author Samandar Daminov
 * Date: 9/27/2024
 */
@Getter
public class CustomException extends RuntimeException {

    private final int code;
    private final String message;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.name());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public CustomException(HttpStatusCode httpStatusCode, String message) {
        super(String.format("%s: %s", httpStatusCode, message));
        this.code = httpStatusCode.value();
        this.message = message;
    }
}
