package me.rubick.transport.app.vo;

import me.rubick.common.app.utils.BeanMapperUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractVo implements Serializable {

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        BeanMapperUtils.copy(this, map);
        return map;
    }
}
