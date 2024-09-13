package uz.xnarx.productservice.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
public record ExceptionDto(ErrorResult error) implements Serializable {
    public record ErrorResult(@JsonProperty("code")
                              int code,
                              @JsonProperty("timestamp")
                              Date date,
                              @JsonProperty("message")
                              String message){

    }
}
