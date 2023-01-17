package ru.sibintek.vme.request.service.constant;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExportMapping {
    public static final Map<String, String> EXPORT_FIELDS_SET;

    static {
        EXPORT_FIELDS_SET = new LinkedHashMap<>();
        EXPORT_FIELDS_SET.put("Идентификатор", "id");
        EXPORT_FIELDS_SET.put("Статус заявки", "state");
        EXPORT_FIELDS_SET.put("Номер заявки", "number");
        EXPORT_FIELDS_SET.put("Дата заявки", "date");
        EXPORT_FIELDS_SET.put("Уникальный номер заявки", "uid");
        EXPORT_FIELDS_SET.put("Тип заявки", "type");
        EXPORT_FIELDS_SET.put("Наименование и характер груза", "typeOfWork");
        EXPORT_FIELDS_SET.put("Вид техники", "typeMachine");
    }
}
