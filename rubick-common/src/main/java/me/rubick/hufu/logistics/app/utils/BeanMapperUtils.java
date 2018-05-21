package me.rubick.hufu.logistics.app.utils;

import me.rubick.hufu.logistics.app.exception.CommonException;
import org.apache.commons.lang3.ObjectUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;

public class BeanMapperUtils {

    private final static Mapper mapper = new DozerBeanMapper();

    public static <T> T map(Object source, Class<T> clazz) {
        return mapper.map(source, clazz);
    }

    public static <T> List<T> mapList(List<?> sourceList, Class<T> clazz) {
        List<T> arr = new ArrayList<>(sourceList.size());

        for (Object t: sourceList) {
            arr.add(mapper.map(t, clazz));
        }

        return arr;
    }
}
