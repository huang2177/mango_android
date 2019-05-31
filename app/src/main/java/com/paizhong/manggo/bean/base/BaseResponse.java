package com.paizhong.manggo.bean.base;

import java.io.Serializable;

/**
 * Created by zab on 2018/3/26 0026.
 */

public class BaseResponse<T> implements Serializable {
    public boolean success;
    public String errorCode;
    public String errorInfo;
    public String time;
    public T data;
}
