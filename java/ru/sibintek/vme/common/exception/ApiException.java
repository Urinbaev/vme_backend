package ru.sibintek.vme.common.exception;

public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }
}
