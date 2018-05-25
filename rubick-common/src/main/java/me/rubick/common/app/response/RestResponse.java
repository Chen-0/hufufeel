package me.rubick.common.app.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class RestResponse<T> implements Serializable {

    private boolean success = false;

    private String message;

    private T data;

    public RestResponse() {
        success = true;
    }

    public RestResponse(String message) {
        success = false;
        this.message = message;
    }

    public RestResponse(T data) {
        success = true;
        this.data = data;
    }
}
