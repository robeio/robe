package io.robe.admin.hibernate.entity;

import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.Paranamer;
import io.robe.admin.RobeApplication;
import io.robe.common.utils.StringsOperations;
import org.joda.time.DateTime;
import org.junit.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Created by hasanmumin on 07/10/2016.
 */
public class EntityTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityTest.class);
    private static final Paranamer paranamer = new AdaptiveParanamer();

    private static Object getDefaultValueByType(Class<?> type, int i) {

        if (type.isEnum()) {
            return type.getEnumConstants()[0];
        }

        switch (type.getTypeName()) {
            case "java.lang.String":
                return "robe" + i;
            case "int":
                return i;
            case "short":
                return (short) i;
            case "char[]":
                return ("robe" + i).toCharArray();
            case "boolean":
                return i % 2 == 0;
            case "java.util.Date":
                return new DateTime().plusMinutes(i).toDate();

            default: {
                LOGGER.info("please handle {}", type.getTypeName());
            }
        }

        return null;
    }

    public void defaultMethods(Class<?> clazz) throws IllegalAccessException, InstantiationException {

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

                Object value = getDefaultValueByType(parameterType, i);

                fieldSetter.invoke(instance, value);

                Method fieldGetter;

                if (parameterType.getTypeName().equals("boolean")) {
                    fieldGetter = clazz.getMethod("is" + StringsOperations.capitalizeFirstChar(fieldName));
                } else {
                    fieldGetter = clazz.getMethod("get" + StringsOperations.capitalizeFirstChar(fieldName));
                }

                Object result = fieldGetter.invoke(instance);

                assertTrue(result.equals(value));

            } catch (NoSuchMethodException e) {
                LOGGER.error("NoSuchMethodException {} ", e.getMessage()); // TODO ignore this exception
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.error(e.getMessage());
                assert false;
            }
        }
    }

    public void constructor(Class<?> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            String[] names = paranamer.lookupParameterNames(constructor);
            if (names.length > 0) {
                Parameter[] parameters = constructor.getParameters();
                Object[] arr = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    Object value = getDefaultValueByType(parameter.getType(), i);
                    arr[i] = value;
                }
                Object instance = constructor.newInstance(arr);

                for (int i = 0; i < names.length; i++) {
                    String name = names[i];

                    Method fieldGetter;
                    if (parameters[i].getType().getTypeName().equals("boolean")) {
                        fieldGetter = clazz.getMethod("is" + StringsOperations.capitalizeFirstChar(name));
                    } else {
                        fieldGetter = clazz.getMethod("get" + StringsOperations.capitalizeFirstChar(name));
                    }

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

    @Test
    public void entityGetterSetterTest() {

        Reflections reflections = new Reflections("io.robe.admin.hibernate.entity", RobeApplication.class.getClassLoader());
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(javax.persistence.Entity.class);


        for (Class clazz : classes) {
            try {
                this.constructor(clazz);
                this.defaultMethods(clazz);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                LOGGER.error(e.getMessage());
            }
        }

    }
}
