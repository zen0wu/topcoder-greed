package greed.util.reflect;

import greed.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Greed is good! Cheers!
 */
public class ReflectionUtil {
    public static Method findMethod(Class<?> clazz, String methodName, Class<?> returnType, int paramNum) {
        Method found = null;
        for (Method method: clazz.getDeclaredMethods()) {
            // Note the co-variance on the return type
            if (method.getName().equals(methodName) && returnType.isAssignableFrom(method.getReturnType()) && method.getParameterTypes().length == paramNum) {
                if (found == null)
                    found = method;
                else {
                    Log.e(String.format("Ambiguous method named %s(%d): %s", methodName, paramNum, returnType.getSimpleName()));
                }
            }
        }
        return found;
    }

    public static Method findSetter(Class<?> clazz, Field field) {
        String methodName = "set" + normalizeFieldName(field.getName());
        for (Method method: clazz.getMethods()) {
            if (method.getName().equals(methodName))
                return method;
        }
        return null;
    }

    public static String normalizeEnumName(String name) {
        StringBuilder concatenated = new StringBuilder();
        for (String tok: name.split("-")) {
            if (concatenated.length() > 0)
                concatenated.append('_');
            concatenated.append(tok.toUpperCase());
        }
        return concatenated.toString();
    }

    private static String normalizeFieldName(String name) {
        StringBuilder concatenated = new StringBuilder();
        for (String tok: name.split("-")) {
            concatenated.append(tok.substring(0, 1).toUpperCase()).append(tok.substring(1));
        }
        return concatenated.toString();
    }
}
