package ru.sibintek.vme.request.controller.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Фактические данные заявки.")
public class FactDataDto {
    @Schema(description = "модель, марка ТС")
    private String model;
    @Schema(description = "пробег ТС")
    private Double mileage;
    @Schema(description = "моточас ТС")
    private Double motoHour;
    @Schema(description = "машиночас")
    private Double moHour;
    @Schema(description = "количество смен")
    private Integer shiftCount;
    @Schema(description = "количество пассажиров")
    private Integer passengerCount;
    @Schema(description = "пробег с грузом")
    private Double mileageCargo;
    @Schema(description = "масса груза")
    private Double massCargo;
    @Schema(description = "а и время выезда из гараж/убытие из предыдущего места работ")
    private String feedDate;
    @Schema(description = "Дата и время заезда в гараж/прибытие на следующее место работ")
    private String returnDate;
    @Schema(description = "Дата и время прибытия к текущему месту работ")
    private String feedPointDate;
    @Schema(description = "Дата и время убытия из текущего места работ")
    private String returnPointDate;
    @Schema(description = "Нулевой пробег суммарный")
    private Double idleMileage;
    @Schema(description = "Нулевой моточас, суммарный")
    private Double idleMoHours;
    @Schema(description = "Идентификатор назначенного транспорта ЕКТП")
    private Integer idTransportContractor;
    @Schema(description = "Идентификатор назначенного транспорта СМТ")
    private Integer idTransportExternal;
    @Schema(description = "Фактические данные верхнего оборудования")
    private List<FactDataTopDto> tops;

}
