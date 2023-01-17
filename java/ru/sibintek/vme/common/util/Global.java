package ru.sibintek.vme.common.util;

import java.util.Map;

public class Global {

    public static String getStr(Map<String, Object> map, String key) {
        if (map == null) {
            return null;
        } else {
            Object value = map.get(key);
            if (value == null) {
                return null;
            } else if (!(value instanceof String)) {
                throw new ClassCastException("Entry " + key + " contains non-string value.");
            } else {
                return value.toString();
            }
        }
    }

    public static Integer parseInteger(String value, Integer defValue) {
        if (value != null && value.length() != 0) {
            try {
                return Integer.valueOf(value);
            } catch (Exception var3) {
                return defValue;
            }
        } else {
            return defValue;
        }
    }

    public static Long parseLong(String value, Long defValue) {
        if (value != null && value.length() != 0) {
            try {
                return Long.valueOf(value);
            } catch (Exception var3) {
                return defValue;
            }
        } else {
            return defValue;
        }
    }
}
