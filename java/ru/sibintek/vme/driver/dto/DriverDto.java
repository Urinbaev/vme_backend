package ru.sibintek.vme.driver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverDto {
    private Long id;
    private String personnelNumber;
    private String fio;
    private String contactInfo;
    private boolean active;
}