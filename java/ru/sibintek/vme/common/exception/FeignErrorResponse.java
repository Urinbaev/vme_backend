package ru.sibintek.vme.common.exception;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FeignErrorResponse {

    private String code;
    private String title;
    private String message;
    private Map<String, List<String>> fields = Map.of();

}
