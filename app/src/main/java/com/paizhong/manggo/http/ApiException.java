package com.paizhong.manggo.http;



public class ApiException extends RuntimeException {

    private String message;
    private int errorCode = -1;
    private String time;

    public ApiException(String message) {
        this.message = message;
    }

    public ApiException(String message, int code) {
        this.message = message;
        this.errorCode = code;
    }

    public ApiException(String message, int code, String time) {
        this.message = message;
        this.errorCode = code;
        this.time = time;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return errorCode;
    }

    public void setCode(int code) {
        this.errorCode = code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     * @param code
     * @return
     */
    private String getApiExceptionMessage(int code) {
        switch (code) {
            default:
                message = "未知错误";
        }
        return message;
    }
}
