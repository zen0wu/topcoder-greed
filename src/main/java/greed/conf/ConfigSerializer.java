package greed.conf;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import greed.conf.meta.ConfigObjectClass;
import greed.conf.meta.Conflict;
import greed.conf.meta.MapParam;
import greed.conf.meta.Required;
import greed.conf.schema.GreedConfig;
import greed.util.Configuration;
import greed.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Greed is good! Cheers!
 */
public class ConfigSerializer {

    public ConfigSerializer() {}

    @SuppressWarnings("unchecked")
    public <T> T serializeAndCheck(String path, Object obj, Class<T> configClass) throws ConfigException {
        if (String.class.equals(configClass)) {
            return (T) obj.toString();
        }
        else if (Integer.TYPE.equals(configClass) || Integer.class.equals(configClass)) {
            try {
                return (T) Integer.valueOf(Integer.parseInt(obj.toString()));
            }
            catch (NumberFormatException e) {
                throw new ConfigException(String.format("Cannot parse integer for %s from %s", path, obj.toString()), e);
            }
        }
        else if (Boolean.TYPE.equals(configClass) || Boolean.class.equals(configClass)) {
            return (T) Boolean.valueOf(Boolean.parseBoolean(obj.toString()));
        }

        if (!(obj instanceof Config) && !(obj instanceof ConfigObject)) {
            throw new ConfigException(String.format("Config object needed, %s found", obj.getClass().getSimpleName()));
        }

        Config rawConf = (obj instanceof ConfigObject) ? ((ConfigObject) obj).toConfig() : (Config) obj;
        try {
            T configObject = configClass.newInstance();
            for (Field field: configClass.getDeclaredFields()) {
                if (!rawConf.hasPath(field.getName())) {
                    if (field.isAnnotationPresent(Required.class))
                        throw new ConfigException(String.format("Missing required config key at %s.%s", path, field.getName()));
                    continue;
                }

                Method setter = findSetter(configClass, field.getName());
                if (setter == null) {
                    throw new ConfigException(String.format("FATAL: Unable to find setter for %s in class %s", field.getName(), configClass.getName()));
                }

                Class<?> fieldType = field.getType();
                if (fieldType.isPrimitive() || String.class.equals(fieldType)) {
                    setter.invoke(configObject, serializeAndCheck(path + "." + field.getName(), rawConf.getAnyRef(field.getName()), fieldType));
                }
                else if (fieldType.isArray()) {
                    Class<?> elementType = fieldType.getComponentType();
                    List<?> rawValues = rawConf.getAnyRefList(field.getName());
                    Object values = Array.newInstance(elementType, rawValues.size());
                    int idx = 0;
                    for (Object rv: rawValues) {
                        Array.set(values, idx, serializeAndCheck(String.format("%s.[%d]", path, idx), rv, elementType));
                        idx++;
                    }
                    setter.invoke(configObject, values);
                }
                else if (field.isAnnotationPresent(MapParam.class)) {
                    HashMap<Object, Object> map = new HashMap<Object, Object>();
                    Class<?> valueClass = field.getAnnotation(MapParam.class).value();
                    for (Map.Entry<String, ConfigValue> entry : rawConf.getObject(field.getName()).entrySet()) {
                        map.put(entry.getKey(),
                                serializeAndCheck(path + "." + field.getName() + "." + entry.getKey(), entry.getValue(), valueClass));
                    }
                    setter.invoke(configObject, map);
                }
                else if (fieldType.isAnnotationPresent(ConfigObjectClass.class)) {
                    setter.invoke(configObject, serializeAndCheck(path + "." + field.getName(), rawConf.getConfig(field.getName()), fieldType));
                }
                else {
                    throw new ConfigException(String.format("Field %s with unknown type %s at path %s", field.getName(), fieldType.getSimpleName(), path));
                }
            }

            // Check for conflict
            for (Field field: configClass.getDeclaredFields()) {
                Method getter = findGetter(configClass, field.getName());
                if (getter.invoke(configObject) == null)
                    continue;

                if (field.isAnnotationPresent(Conflict.class)) {
                    for (String conflictField: field.getAnnotation(Conflict.class).withField()) {
                        Method cgetter = findGetter(configClass, conflictField);
                        if (cgetter.invoke(configObject) != null) {
                            throw new ConfigException(String.format("Conflict fields of %s and %s at path %s", field.getName(), conflictField, path));
                        }
                    }
                }
            }

            return configObject;
        } catch (InstantiationException e) {
            throw new ConfigException("FATAL: Exception while reflecting on " + configClass.getName(), e);
        } catch (IllegalAccessException e) {
            throw new ConfigException("FATAL: Exception while reflecting on " + configClass.getName(), e);
        } catch (InvocationTargetException e) {
            throw new ConfigException("FATAL: Exception while reflecting on " + configClass.getName(), e);
        }
    }

    private Method findGetter(Class<?> clazz, String fieldName) {
        String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        for (Method method: clazz.getMethods()) {
            if (method.getName().equals(methodName))
                return method;
        }
        return null;
    }

    private Method findSetter(Class<?> clazz, String fieldName) {
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        for (Method method: clazz.getMethods()) {
            if (method.getName().equals(methodName))
                return method;
        }
        return null;
    }
}
