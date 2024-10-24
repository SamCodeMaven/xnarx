package uz.xnarx.productservice.payload.enums;

import lombok.Getter;


@Getter

public enum ErrorCode {

    EMAIL_NOT_VALID(400,"Email is not valid"),
    ENTITY_NOT_FOUND(404,"Entity not found"),
    VERIFICATION_TOKEN_NOT_FOUND(404, "Verification token not found"),
    TOKEN_ALREADY_EXPIRED(401, "Verification token expired"),
    ENTITY_ALREADY_EXISTS(409,"Entity already exists"),
    TOKEN_ALREADY_CONFIRMED(409, "Token already confirmed"),
    FAILED_TO_SEND_MESSAGE(409,"Failed to send message"),;


    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
