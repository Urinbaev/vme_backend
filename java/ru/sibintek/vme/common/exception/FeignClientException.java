package ru.sibintek.vme.common.exception;

public class FeignClientException extends RuntimeException {

    private static final long serialVersionUID = 3902162121293508769L;

    public FeignClientException(String message) {
        super(message);
    }

}
