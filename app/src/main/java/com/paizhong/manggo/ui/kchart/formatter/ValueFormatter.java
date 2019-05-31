package com.paizhong.manggo.ui.kchart.formatter;

/**
 * Value格式化类
 */
public class ValueFormatter implements IValueFormatter {

    @Override
    public String format(float value) {
        return String.format("%.2f", value);
    }

}
