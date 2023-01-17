package ru.sibintek.vme.request.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Фактические данные верхнего оборудования")
public class FactDataTopDto {
    @Schema(description = "Наименование верхнего оборудования")
    private String model;
    @Schema(description = "Наработка")
    private Double value;
    @Schema(description = "Название операции")
    private String[] operName;
    @Schema(description = "Обозначение операции")
    private String[] mUnit;
}
