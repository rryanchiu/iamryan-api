package me.rryan.tinyurl.common;

public enum ResponseCode {
    EXCEPTION(1000, "Operation failed, please try again later."),
    IP_LIMIT(1001, "Request too frequency, please try again later");

    private int code;

    private String message;

    ResponseCode() {
    }

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
