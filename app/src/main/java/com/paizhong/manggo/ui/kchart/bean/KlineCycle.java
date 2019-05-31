package com.paizhong.manggo.ui.kchart.bean;

import java.io.Serializable;

public class KlineCycle implements Serializable {

    private String name;
    private String code;
    private String timeCode;
    private int time;
    private int normal;
    public KlineCycle() {
    }


    public KlineCycle(String name, String code,String timeCode) {
        this.name = name;
        this.code = code;
        this.timeCode = timeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTimeCode() {
        return timeCode;
    }

    public void setTimeCode(String timeCode) {
        this.timeCode = timeCode;
    }
}
