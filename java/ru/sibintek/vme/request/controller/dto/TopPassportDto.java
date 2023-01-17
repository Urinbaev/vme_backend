package ru.sibintek.vme.request.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = "Описание оборудования (верхнее, навесное)")
public class TopPassportDto {
    @Schema(description = "Идентификатор")
    Integer id;
    @Schema(description = "Код мобильного объекта, на котором установлено")
    Integer idMO;
    @Schema(description = "Модель оборудования")
    String model;
    @Schema(description = "Тип/вид оборудования.")
    String type;
    @Schema(description = "Вид ставки тарифа (перечисление, если допустимы одновременно): маш-час, мото-час")
    String typeRate;
    @Schema(description = "Описание")
    String descr;
}