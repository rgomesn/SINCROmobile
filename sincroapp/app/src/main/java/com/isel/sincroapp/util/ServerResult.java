package com.isel.sincroapp.util;

public class ServerResult<T> {
    private int code;
    private String error;
    private int stringCode;
    private T result;
    private boolean isOk;

    public ServerResult(int code, String error) {
        this.code = code;
        this.error = error;
        isOk = false;
    }

    public ServerResult(int code, int stringCode) {
        this.code = code;
        this.stringCode = stringCode;
        isOk = false;
    }

    public ServerResult(int code, T result) {
        this.code = code;
        this.result = result;
        isOk = true;
    }

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    public int getStringCode() {
        return stringCode;
    }

    public T getResult() {
        return result;
    }

    public boolean isOk() {
        return isOk;
    }
}
