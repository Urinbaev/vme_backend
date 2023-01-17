package ru.sibintek.vme.request.controller.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Модель класса для отправки данных в очередь для назначения ТС.
 */
@Data
@Schema(description = "Модель класса для отправки данных в очередь для назначения ТС")
public class AssignTSDto {
    @Schema(description = "Код мобильного объекта")
    private Long idTs;
    @Schema(description = "Массив профилей терминалов бортового оборудования")
    private List<ProfileDto> boProfiles = new ArrayList<>();
    @Schema(description = "Рег. знак ТС")
    private String regNumber;
    @Schema(description = "Марка ТС")
    private String mark;
    @Schema(description = "Модель ТС")
    private String model;
    @Schema(description = "Тип ТС")
    private String type;
    @Schema(description = "Массив описаний оборудования (верхнее, навесное)")
    private List<TopPassportDto> topPassports = new ArrayList<>();
    @Schema(description = "Контактная информация водителя")
    private String driverContactInfo;
    @JsonIgnore
    @Schema(description = "Внутренний идентификатор водителя")
    private Long driverId;
    @Schema(description = "Имя и отчество водителя")
    private String driverFio;
    @Schema(description = "Имя и отчество второго водителя")
    private String driverFio2;
    @Schema(description = "Серия, номер и дата путевого листа")
    private String routeList;
    @Schema(description = "Категория ТС / мобильного объекта (по ГОСТ)")
    private String category;
    @Schema(description = "Идентификатор назначенного ТС мобильного объекта на стороне исполнителя")
    private Integer idTransportContractor;

}
