package uk.gov.justice.laa.crime.application.tracking.helper;

import java.lang.reflect.Field;
import org.springframework.util.ReflectionUtils;

public class ReflectionHelper {
    private ReflectionHelper() {}

    public static <T> void updateEntityFromObject(T entityToUpdate, T object) {
        for (Field declaredField : object.getClass().getDeclaredFields()) {
            ReflectionUtils.makeAccessible(declaredField);
            Object fieldValue = ReflectionUtils.getField(declaredField, object);

            if (fieldValue != null) {
                ReflectionUtils.setField(declaredField, entityToUpdate, fieldValue);
            }
        }
    }
}
