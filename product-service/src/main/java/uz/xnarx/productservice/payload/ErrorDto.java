package uz.xnarx.productservice.payload;

/**
 * @author Samandar Daminov
 * Date: 9/27/2024
 */

public record ErrorDto(
        int code,
        String message
) {
}
