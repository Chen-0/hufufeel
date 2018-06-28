package me.rubick.common.app.utils;

import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BeanMapperUtils {

    private final static Mapper mapper = new DozerBeanMapper();

    public static <T> T map(Object source, Class<T> clazz) {
        return mapper.map(source, clazz);
    }

    public static <T> List<T> mapList(List<?> sourceList, Class<T> clazz) {
        List<T> arr = new ArrayList<>(sourceList.size());

        for (Object t : sourceList) {
            arr.add(mapper.map(t, clazz));
        }

        return arr;
    }

    public static void copy(Object src, Object dest) {
        mapper.map(src, dest);
    }

    public static void setDefault(Object o, String fieldName) {
        try {
            Method getMethod = getGetMethod(o.getClass(), fieldName);
            Method setMethod = getSetMethod(o.getClass(), fieldName);
            if (ObjectUtils.isEmpty(getMethod.invoke(o))) {
                Field field = o.getClass().getDeclaredField(fieldName);
                Class clazz = field.getType();
                System.out.println(clazz.getName());
                if (BigDecimal.class == clazz) {
                    setMethod.invoke(o, new Object[]{BigDecimal.ZERO});
                } else if (clazz == String.class ) {
                    setMethod.invoke(o, new Object[]{""});
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Method getGetMethod(Class objectClass, String fieldName) throws NoSuchMethodException {
        StringBuffer sb = new StringBuffer();
        sb.append("get");
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        return objectClass.getMethod(sb.toString());
    }

    @SuppressWarnings("unchecked")
    private static Method getSetMethod(Class objectClass, String fieldName) throws NoSuchFieldException, NoSuchMethodException {
        Class[] parameterTypes = new Class[1];
        Field field = objectClass.getDeclaredField(fieldName);
        parameterTypes[0] = field.getType();
        StringBuffer sb = new StringBuffer();
        sb.append("set");
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        Method method = objectClass.getMethod(sb.toString(), parameterTypes);
        return method;
    }
}
