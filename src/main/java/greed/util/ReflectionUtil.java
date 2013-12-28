package greed.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Greed is good! Cheers!
 */
public class ReflectionUtil {
    public static Method findMethod(Class<?> clazz, String methodName, int paramNum) {
        Method found = null;
        for (Method method: clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && method.getParameterTypes().length == paramNum) {
                if (found == null)
                    found = method;
                else {
                    // TODO: support for raw logging (logging to stderr before the log is configured)
                    //Log.w("Ambigious method named " + methodName + " with " + paramNum + " parameters");
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

    private static String normalizeFieldName(String name) {
        StringBuilder concatenated = new StringBuilder();
        for (String tok: name.split("-")) {
            concatenated.append(tok.substring(0, 1).toUpperCase()).append(tok.substring(1));
        }
        return concatenated.toString();
    }
}
