package uz.xnarx.productservice.payload.enums;

public enum ErrorCode {

    ENTITY_NOT_FOUND("404"),
    ENTITY_ALREADY_EXISTS("409");


    private final String code;

    ErrorCode(String code) {
        this.code = code;

    }

    public String getCode() {
        return code;
    }

}
