package io.robe.common;

import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.Paranamer;
import io.robe.common.utils.StringsOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;

import static org.junit.Assert.assertTrue;

/**
 * Created by hasanmumin on 26/09/16.
 * <p>
 * This class under development, not complete yet.
 */
public class TestUtils {


    private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);

    private static final Paranamer paranamer = new AdaptiveParanamer();


    public static void privateConstructor(Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
        constructor.setAccessible(false);
    }

    public static void constructor(Class<?> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            String[] names = paranamer.lookupParameterNames(constructor);
            if (names.length > 0) {
                Parameter[] parameters = constructor.getParameters();
                Object[] arr = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    Object value = getDefaultValueByType(parameter.getType().getTypeName(), i);
                    arr[i] = value;
                }
                Object instance = constructor.newInstance(arr);

                for (int i = 0; i < names.length; i++) {
                    String name = names[i];
                    Method fieldGetter = clazz.getMethod("get" + StringsOperations.capitalizeFirstChar(name));
                    Object value = fieldGetter.invoke(instance);
                    assertTrue(value.equals(arr[i]));
                }

                instance.toString();

            } else {
                Object instance = constructor.newInstance();
                instance.toString();
            }
        }
    }

    public static void defaultMethods(Class<?> clazz) throws IllegalAccessException, InstantiationException {

        Object instance = clazz.newInstance();
        Field[] allFields = clazz.getDeclaredFields();

        String[] fieldNames = new String[allFields.length];
        Class[] parameters = new Class[allFields.length];

        for (int i = 0; i < allFields.length; i++) {
            fieldNames[i] = allFields[i].getName();
            parameters[i] = allFields[i].getType();
        }


        for (int i = 0; i < fieldNames.length; i++) {
            String fieldName = fieldNames[i];
            Class parameterType = parameters[i];
            try {
                Method fieldSetter = clazz.getMethod("set" + StringsOperations.capitalizeFirstChar(fieldName), parameterType);

                Object value = getDefaultValueByType(parameterType.getTypeName(), i);

                fieldSetter.invoke(instance, value);

                Method fieldGetter = clazz.getMethod("get" + StringsOperations.capitalizeFirstChar(fieldName));

                Object result = fieldGetter.invoke(instance);

                assertTrue(result.equals(value));

            } catch (NoSuchMethodException e) {
                LOGGER.error("NoSuchMethodException {}", e.getMessage()); // TODO ignore this exception
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.error(e.getMessage());
                assert false;
            }
        }
    }

    private static Object getDefaultValueByType(String type, int i) {
        switch (type) {
            case "java.lang.String":
                return "robe" + i;

            default: {
                LOGGER.info("please handle {}", type);
            }
        }

        return null;
    }
}
