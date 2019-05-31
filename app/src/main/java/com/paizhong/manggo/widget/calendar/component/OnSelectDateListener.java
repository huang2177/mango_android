package com.paizhong.manggo.widget.calendar.component;

import com.paizhong.manggo.widget.calendar.model.CalendarDate;

/**
 * Created by pengpei.weipan.widget on 17/6/2.
 */

public interface OnSelectDateListener {
    void onSelectDate(CalendarDate date);

    void onSelectOtherMonth(int offset);//点击其它月份日期
}
