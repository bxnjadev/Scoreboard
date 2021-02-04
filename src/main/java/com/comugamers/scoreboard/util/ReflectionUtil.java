package com.comugamers.scoreboard.util;

import java.lang.reflect.Field;

public class ReflectionUtil {

    public static void setField(Object edit, String fieldName, Object value) {
        try {
            Field field = edit.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(edit, value);
        }

        catch (NoSuchFieldException|IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
