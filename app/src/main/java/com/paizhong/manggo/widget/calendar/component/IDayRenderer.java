package com.paizhong.manggo.widget.calendar.component;

import android.graphics.Canvas;

import com.paizhong.manggo.widget.calendar.view.Day;


/**
 * Created by ldf on 17/6/26.
 */

public interface IDayRenderer {

    void refreshContent();

    void drawDay(Canvas canvas, Day day);

    IDayRenderer copy();

}
