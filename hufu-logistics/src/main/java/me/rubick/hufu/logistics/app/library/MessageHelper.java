package me.rubick.hufu.logistics.app.library;


import me.rubick.common.app.utils.JSONMapper;

import java.util.HashMap;
import java.util.Map;

public class MessageHelper {
    public static String toSuccess() {
        return toSuccess(null, null);
    }

    public static String toError() {
        return toError(null, null);
    }

    public static String toError(String info) {
        return toError(info, null);
    }

    public static String toSuccess(String info, Object data) {
        return toAjax(MessageCode.SUCCESS.getCode(), info, data);
    }

    public static String toError(String info, Object data) {
        return toAjax(MessageCode.ERROR.getCode(), info, data);
    }

    private static String toAjax(Integer code, String info, Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("info", info);
        result.put("data", data);
        return JSONMapper.toJSON(result);
    }
}
