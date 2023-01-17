package ru.sibintek.vme.common.exception;

public class RecordNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 2068084954141411366L;

    public RecordNotFoundException(String message) {
        super(message);
    }

}
