package org.narcissus.narcissuscoreservice.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NullFieldChecker {

    public static List<String> check (Object object) {
        return check(object, new ArrayList<>());
    }

    public static List<String> check (Object object, String... allowedNullFields) {
        return check(object, Arrays.asList(allowedNullFields));
    }

    public static List<String> check(Object object, List<String> allowedNullFields) {
        List<String> nullList = new ArrayList<>();
        if (object != null) {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    if (field.get(object) == null && !allowedNullFields.contains(field.getName())) {
                        nullList.add(field.getName());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return nullList;
    }

    // public static List<String> check(Object object) {
    //     List<String> nullList = new ArrayList<>();
    //     if (object != null) {
    //         for (Field field : object.getClass().getDeclaredFields()) {
    //             field.setAccessible(true);
    //             try {
    //                 if (field.get(object) == null) {
    //                     nullList.add(field.getName());
    //                 }
    //             } catch (IllegalAccessException e) {
    //                 e.printStackTrace();
    //             }
    //         }
    //     }
    //     return nullList;
    // }
}
