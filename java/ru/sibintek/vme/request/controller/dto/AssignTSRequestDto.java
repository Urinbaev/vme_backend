package ru.sibintek.vme.request.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Модель для назначения ТС на заявку.
 */
@Data
@Schema(description = "Модель для назначения ТС на заявку.")
public class AssignTSRequestDto {

    @NotNull
    @Schema(description = "Внутренний идентификатор ТС")
    private Long tsId;

    @Schema(description = "Внутренний идентификатор водителя")
    private Long driverId;

    @Schema(description = "ФИО водителя")
    private String driverFio;

    @Schema(description = "Контактная информация водителя")
    private String driverContactInfo;

}