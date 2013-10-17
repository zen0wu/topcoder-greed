package greed.conf;

import com.typesafe.config.Config;
import greed.conf.meta.Required;
import greed.conf.schema.GreedConfig;
import greed.util.Configuration;
import greed.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Greed is good! Cheers!
 */
public class ConfigSerializer {

    private static GreedConfig theConfig = null;

    public static void reloadConfig() throws ConfigException {
        theConfig = null;
        ConfigSerializer serializer = new ConfigSerializer();
        theConfig = serializer.serializeAndCheck("greed", Configuration.getConfig().getConfig("greed"), GreedConfig.class);
    }

    public static GreedConfig getGreedConfig() throws ConfigException {
        if (theConfig == null) {
            reloadConfig();
        }
        return theConfig;
    }

    public <T> T serializeAndCheck(String path, Config conf, Class<T> configClass) throws ConfigException {
        try {
            T configObject = configClass.newInstance();
            for (Field field: configClass.getFields()) {
                if (field.isAnnotationPresent(Required.class) && !conf.hasPath(field.getName())) {
                    throw new ConfigException(String.format("Missing required config key at %s.%s", path, field.getName()));
                }

                Method setter = findSetter(configClass, field.getName());
                Class<?> fieldType = field.getType();

                if (String.class.equals(fieldType)) {
                    setter.invoke(configObject, conf.getString(field.getName()));
                }
                else if (Integer.TYPE.equals(fieldType)) {
                    setter.invoke(configObject, conf.getInt(field.getName()));
                }
                else if (Boolean.TYPE.equals(fieldType)) {
                    setter.invoke(configObject, conf.getBoolean(field.getName()));
                }
                else if (fieldType.isArray()) {
                    Class<?> elementType = fieldType.getComponentType();
                }
                else {
                    
                }
            }
            return configObject;
        } catch (InstantiationException e) {
            Log.e("Unable to reflect on class " + configClass.getName(), e);
            throw new ConfigException("Exception while reflecting on " + configClass.getName(), e);
        } catch (IllegalAccessException e) {
            Log.e("Unable to reflect on class " + configClass.getName(), e);
            throw new ConfigException("Exception while reflecting on " + configClass.getName(), e);
        } catch (InvocationTargetException e) {
            Log.e("Unable to reflect on class " + configClass.getName(), e);
            throw new ConfigException("Exception while reflecting on " + configClass.getName(), e);
        }
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
