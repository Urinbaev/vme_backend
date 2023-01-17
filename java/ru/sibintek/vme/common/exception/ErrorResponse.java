package ru.sibintek.vme.common.exception;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ErrorResponse {

    private final String code;
    private final String name;
    private final String description;
    private final String error;
    Map<String, List<String>> errors;

}
