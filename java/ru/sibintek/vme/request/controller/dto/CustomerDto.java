package ru.sibintek.vme.request.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель данных заказчика.")
public class CustomerDto {
    @Schema(description = "Номер служебного телефона")
    private String phone;
    @Schema(description = "Наименование организации")
    private String organization;
    @Schema(description = "Имя и отчество ответственного за заявку")
    private String fio;
    @Schema(description = "Идентификатор организации")
    private int id;
    @Schema(description = "true - заказчиком является клиент, который прислал заявку. false - заказчиком является сторонний контрагент")
    private boolean own;
}
