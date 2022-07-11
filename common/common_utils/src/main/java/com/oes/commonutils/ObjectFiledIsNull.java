package com.oes.commonutils;

import java.lang.reflect.Field;

public class ObjectFiledIsNull {
    public static Result isFiledHaveNull(Object object, String message) {
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.getName().equals("id")) continue;
                if ((field.get(object) == null && !field.getName().equals("id")) || field.get(object).equals("")) {
                    return Result.error().message(message);
                }
            } catch (IllegalAccessException e) {
                return Result.error().message(message);
            }
        }
        return Result.ok();
    }
}