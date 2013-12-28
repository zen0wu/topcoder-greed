package greed.conf;

import greed.conf.meta.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import greed.conf.parser.IParser;
import greed.util.reflect.ReflectionUtil;

/**
 * Greed is good! Cheers!
 */
public class ConfigParser {

    public ConfigParser() {}

    @SuppressWarnings("unchecked")
    public <T> T parseAndCheck(String path, Object obj, Class<T> configClass) throws ConfigException {
        if (String.class.equals(configClass)) {
            return (T) parseString(obj);
        }
        else if (Integer.TYPE.equals(configClass) || Integer.class.equals(configClass)) {
            return (T) parseInt(path, obj);
        }
        else if (Boolean.TYPE.equals(configClass) || Boolean.class.equals(configClass)) {
            return (T) parseBoolean(obj);
        }
        else if (configClass.isEnum()) {
            return (T) parseEnum(obj, configClass);
        }

        if (!(obj instanceof Config) && !(obj instanceof ConfigObject)) {
            throw new ConfigException(String.format("Config object needed, %s found", obj.getClass().getSimpleName()));
        }

        Config rawConf = (obj instanceof ConfigObject) ? ((ConfigObject) obj).toConfig() : (Config) obj;
        HashSet<String> hasSet = new HashSet<String>();
        try {
            T configObject = configClass.newInstance();
            for (Field field: configClass.getDeclaredFields()) {

                if (field.isSynthetic()) {
                    // ignore synthetic fields (e.g. eclEmma)
                    continue;
                }

                if (!rawConf.hasPath(field.getName())) {
                    if (field.isAnnotationPresent(Required.class))
                        throw new ConfigException(String.format("Missing required config key at %s.%s", path, field.getName()));
                    continue;
                }

                Method setter = ReflectionUtil.findSetter(configClass, field);
                if (setter == null) {
                    throw new ConfigException(String.format("FATAL: Unable to find setter for %s in class %s", field.getName(), configClass.getName()));
                }

                IParser parser = null;
                if (field.isAnnotationPresent(Parser.class)) {
                    Class<? extends IParser> parserClass = field.getAnnotation(Parser.class).value();
                    parser = parserClass.newInstance();
                }

                Class<?> fieldType = field.getType();
                hasSet.add(field.getName());
                if (fieldType.isPrimitive() || String.class.equals(fieldType) || fieldType.isEnum()) {
                    setter.invoke(configObject,
                            parser != null ? parser.apply(rawConf.getAnyRef(field.getName())) : parseAndCheck(path + "." + field.getName(), rawConf.getAnyRef(field.getName()), fieldType));
                }
                else if (fieldType.isArray()) {
                    Class<?> elementType = fieldType.getComponentType();
                    List<?> rawValues = rawConf.getAnyRefList(field.getName());
                    Object values = Array.newInstance(elementType, rawValues.size());
                    int idx = 0;
                    for (Object rv: rawValues) {
                        Array.set(values, idx,
                                parser != null ? parser.apply(rv) : parseAndCheck(String.format("%s.[%d]", path, idx), rv, elementType));
                        idx++;
                    }
                    setter.invoke(configObject, values);
                }
                else if (field.isAnnotationPresent(MapParam.class)) {
                    HashMap<Object, Object> map = new HashMap<Object, Object>();
                    Class<?> valueClass = field.getAnnotation(MapParam.class).value();
                    for (Map.Entry<String, ConfigValue> entry : rawConf.getObject(field.getName()).entrySet()) {
                        map.put(entry.getKey(),
                                parser != null ? parser.apply(entry.getValue()) : parseAndCheck(path + "." + field.getName() + "." + entry.getKey(), entry.getValue(), valueClass));
                    }
                    setter.invoke(configObject, map);
                }
                else if (fieldType.isAnnotationPresent(ConfigObjectClass.class)) {
                    setter.invoke(configObject, parseAndCheck(path + "." + field.getName(), rawConf.getConfig(field.getName()), fieldType));
                }
                else if (parser != null) {
                    setter.invoke(configObject, parser.apply(rawConf.getAnyRef(field.getName())));
                }
                else {
                    throw new ConfigException(String.format("Field %s with unknown type %s at path %s", field.getName(), fieldType.getSimpleName(), path));
                }
            }

            // Check for conflict
            for (Field field: configClass.getDeclaredFields()) {
                if (!hasSet.contains(field.getName()))
                    continue;

                if (field.isAnnotationPresent(Conflict.class)) {
                    for (String conflictField: field.getAnnotation(Conflict.class).withField()) {
                        if (hasSet.contains(conflictField)) {
                            throw new ConfigException(String.format("Conflict fields of %s and %s at path %s", field.getName(), conflictField, path));
                        }
                    }
                }
            }

            return configObject;
        } catch (InstantiationException e) {
            throw new ConfigException("FATAL: Exception while reflecting on " + configClass.getName(), e);
        } catch (ReflectiveOperationException e) {
            throw new ConfigException("FATAL: Exception while reflecting on " + configClass.getName(), e);
        }
    }

    private Object parseEnum(Object obj, Class<?> enumClass) throws ConfigException {
        String enumStr = ReflectionUtil.normalizeEnumName(obj.toString());
        Object enumValue = null;
        for (Object ev: enumClass.getEnumConstants())
            if (ev.toString().equals(enumStr)) {
                enumValue = ev;
                break;
            }
        if (enumValue == null)
            throw new ConfigException("No such value of " + enumStr + " for enum type " + enumClass.getName());
        return enumValue;
    }

    private Boolean parseBoolean(Object obj) {
        return Boolean.parseBoolean(obj.toString());
    }

    private Integer parseInt(String path, Object obj) throws ConfigException {
        try {
            return Integer.parseInt(obj.toString());
        }
        catch (NumberFormatException e) {
            throw new ConfigException(String.format("Cannot parse integer for %s from %s", path, obj.toString()), e);
        }
    }

    private String parseString(Object obj) {
        if (obj instanceof ConfigValue) {
            String  s = ((ConfigValue)obj).render() ;
            if (s != null && s.length() >= 2 && s.charAt(0) == '"' && s.charAt(s.length()-1)=='"') {
                s = s.substring(1, s.length() - 1);
            }
            return s;
        } else {
            return obj.toString();
        }
    }
}
