package org.cyclops.commoncapabilities.core;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Helper methods.
 * @author rubensworks
 */
public class Helpers {

    public static <R> R invokeMethod(Object instance, @Nullable Method method, Object... arguments) {
        if (method != null) {
            try {
                return (R) method.invoke(instance, arguments);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <R> R getFieldValue(Object instance, @Nullable Field field) {
        if (field != null) {
            try {
                return (R) field.get(instance);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <E> Method getMethod(Class<? super E> clazz, String method, Class<?>... methodTypes) {
        try {
            return ObfuscationReflectionHelper.findMethod(clazz, method, methodTypes);
        } catch (ObfuscationReflectionHelper.UnableToFindMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> Field getField(Class<? super T> clazz, String field) {
        try {
            return ObfuscationReflectionHelper.findField(clazz, field);
        } catch (ObfuscationReflectionHelper.UnableToFindFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

}
