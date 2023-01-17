package ru.sibintek.vme.common.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.BooleanUtils;

@UtilityClass
public class DataUtils {

    public Object parseData(String data) {
        try {
            return Long.parseLong(data);
        } catch (Exception ignored) {
        }
        try {
            return Double.parseDouble(data);
        } catch (Exception ignored) {
        }
        try {
            return BooleanUtils.toBoolean(data, "true", "false");
        } catch (Exception ignored) {
        }
        return data;
    }
}
