package uz.xnarx.productservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.xnarx.productservice.payload.enums.ErrorCode;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
    private ErrorCode code;
    private String message;
}
