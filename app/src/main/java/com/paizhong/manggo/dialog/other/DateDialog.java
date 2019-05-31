package com.paizhong.manggo.dialog.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.paizhong.manggo.R;
import com.paizhong.manggo.widget.wheelview.AbstractWheelTextAdapter;
import com.paizhong.manggo.widget.wheelview.OnWheelChangedListener;
import com.paizhong.manggo.widget.wheelview.OnWheelScrollListener;
import com.paizhong.manggo.widget.wheelview.WheelView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 日期选择对话框
 */
public class DateDialog extends Dialog {

    private Context context;
    private WheelView wv_start_yearmonth, wv_start_day, wv_end_yearmonth, wv_end_day;
    private TextView btn_myinfo_sure;
//    private View layout_dismiss;
    private ArrayList<String> arry_yearsMonths = new ArrayList<String>(), arry_days = new ArrayList<String>();
    private CalendarTextAdapter mStartYearMonthAdapter, mStartDayAdapter, mEndYearMonthAdapter, mEndDayAdapter;
    private int month, day, currentDay = getDay(), maxTextSize = 15, minTextSize = 12;
    private boolean issetdata = false;
    private String selectStartYearMonth, selectStartDay, selectEndYearMonth, selectEndDay;
    private OnBirthListener onBirthListener;

    public DateDialog(Context context) {
        super(context, R.style.translucent_theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_myinfo_changebirth);
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        WindowManager manage = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        findViewById(R.id.btn_myinfo_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        wv_start_yearmonth = (WheelView) findViewById(R.id.wv_start_yearmonth);
        wv_start_day = (WheelView) findViewById(R.id.wv_start_day);
        wv_end_yearmonth = (WheelView) findViewById(R.id.wv_end_yearmonth);
        wv_end_day = (WheelView) findViewById(R.id.wv_end_day);
        btn_myinfo_sure = (TextView) findViewById(R.id.btn_myinfo_sure);
        btn_myinfo_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBirthListener != null) {
                    String[] splitYear = selectStartYearMonth.split("年");
                    String[] splitMonth = splitYear[1].split("月");
                    String splitDay = selectStartDay.split("日")[0].replace("-","");


                    String[] splitEndYear = selectEndYearMonth.split("年");
                    String[] splitEndMonth = splitEndYear[1].split("月");
                    String splitEndDay = selectEndDay.split("日")[0].replace("-","");

                    String start = splitYear[0]+"-"+((splitMonth[0].length() > 1) ? splitMonth[0] : "0"+splitMonth[0])
                            +"-"+((splitDay.length() > 1) ? splitDay : "0"+splitDay);
                    String end = splitEndYear[0]+"-"+((splitEndMonth[0].length() > 1) ? splitEndMonth[0] : "0"+splitEndMonth[0])
                            +"-"+((splitEndDay.length() > 1) ? splitEndDay : "0"+splitEndDay);
                    onBirthListener.onClick(start,end);
                }
                dismiss();
            }
        });

        if (!issetdata) {
            initData();
        }
        initYearsMonths();
        initDays(day);

        mStartYearMonthAdapter = new CalendarTextAdapter(context, arry_yearsMonths,
                getYearMonthIndex(getYear(), getMonth()), maxTextSize, minTextSize);
        wv_start_yearmonth.setVisibleItems(3);
        wv_start_yearmonth.setViewAdapter(mStartYearMonthAdapter);
        wv_start_yearmonth.setCurrentItem(getYearMonthIndex(getYear(), getMonth()));

        mStartDayAdapter = new CalendarTextAdapter(context, arry_days, currentDay - 7, maxTextSize, minTextSize);
        wv_start_day.setVisibleItems(3);
        wv_start_day.setViewAdapter(mStartDayAdapter);
        wv_start_day.setCurrentItem(currentDay - 8);
        selectStartDay = String.valueOf(currentDay - 7);

        mEndYearMonthAdapter = new CalendarTextAdapter(context, arry_yearsMonths,
                getYearMonthIndex(getYear(), getMonth()), maxTextSize, minTextSize);
        wv_end_yearmonth.setVisibleItems(3);
        wv_end_yearmonth.setViewAdapter(mEndYearMonthAdapter);
        wv_end_yearmonth.setCurrentItem(getYearMonthIndex(getYear(), getMonth()));

        mEndDayAdapter = new CalendarTextAdapter(context, arry_days, currentDay - 1, maxTextSize, minTextSize);
        wv_end_day.setVisibleItems(3);
        wv_end_day.setViewAdapter(mEndDayAdapter);
        wv_end_day.setCurrentItem(currentDay - 1);

        wv_start_yearmonth.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mStartYearMonthAdapter.getItemText(wheel.getCurrentItem());
                selectStartYearMonth = currentText;
                setTextviewSize(currentText, mStartYearMonthAdapter);
                calDays(Integer.parseInt(currentText.substring(0, currentText.indexOf("年"))), Integer.parseInt
                        (currentText.substring(currentText.indexOf("年") + 1, currentText.indexOf("月"))));
                initDays(day);
                mStartDayAdapter = new CalendarTextAdapter(context, arry_days, currentDay - 1, maxTextSize, minTextSize);
                wv_start_day.setVisibleItems(3);
                wv_start_day.setViewAdapter(mStartDayAdapter);
                wv_start_day.setCurrentItem(getDay() - 1);
            }
        });

        wv_start_yearmonth.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mStartYearMonthAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mStartYearMonthAdapter);
            }
        });


        wv_start_day.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mStartDayAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mStartDayAdapter);
                selectStartDay = currentText;
            }
        });

        wv_start_day.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mStartDayAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mStartDayAdapter);
            }
        });

        wv_end_yearmonth.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mEndYearMonthAdapter.getItemText(wheel.getCurrentItem());
                selectEndYearMonth = currentText;
                setTextviewSize(currentText, mEndYearMonthAdapter);
                calDays(Integer.parseInt(currentText.substring(0, currentText.indexOf("年"))), Integer.parseInt
                        (currentText.substring(currentText.indexOf("年") + 1, currentText.indexOf("月"))));
                initDays(day);
                mEndDayAdapter = new CalendarTextAdapter(context, arry_days, currentDay - 1, maxTextSize, minTextSize);
                wv_end_day.setVisibleItems(3);
                wv_end_day.setViewAdapter(mEndDayAdapter);
                wv_end_day.setCurrentItem(getDay() - 1);
            }
        });

        wv_end_yearmonth.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mEndYearMonthAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mEndYearMonthAdapter);
            }
        });

        wv_end_day.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mEndDayAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mEndDayAdapter);
                selectEndDay = currentText;
            }
        });

        wv_end_day.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mEndDayAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mEndDayAdapter);
            }
        });

    }

    public void initYearsMonths() {
        for (int i = 2000; i <= getYear(); i++) {
            for (int j = 1; j <= 12; j++) {
                arry_yearsMonths.add(i + "年" + j + "月");
            }
        }
    }

    public void initDays(int days) {
        arry_days.clear();
        for (int i = 1; i <= days; i++) {
            arry_days.add(i + "日");
        }
    }

    private class CalendarTextAdapter extends AbstractWheelTextAdapter {
        ArrayList<String> list;

        protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }

    }

    public void setBirthdayListener(OnBirthListener onBirthListener) {
        this.onBirthListener = onBirthListener;
    }

    public interface OnBirthListener {
        void onClick(String start, String end);
    }

    public void setTextviewSize(String curriteItemText, CalendarTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(maxTextSize);
            } else {
                textvew.setTextSize(minTextSize);
            }
        }
    }

    public int getYearMonthIndex(int year, int month) {
        int yearIndex = 0;
        int monthIndex = 0;
        if (year != getYear()) {
            this.month = 12;
        } else {
            this.month = getMonth();
        }
        for (int i = 2000; i <= getYear(); i++) {
            if (i == year) {
                break;
            }
            yearIndex++;
        }
        for (int j = 1; j <= getMonth(); j++) {
            if (j == month) {
                break;
            }
            monthIndex++;
        }
        return yearIndex * 12 + monthIndex;
    }

    public int getYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public int getMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    public int getDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DATE);
    }

    public void initData() {
        setStartDate(getYear(), getMonth(), getDay());
        setEndDate(getYear(), getMonth(), getDay());
    }

    public void setStartDate(int year, int month, int day) {
        selectStartYearMonth = year + "年" + month + "月";
        selectStartDay = day + "";
        issetdata = true;
        if (year == getYear()) {
            this.month = getMonth();
        } else {
            this.month = 12;
        }
        calDays(year, month);
    }

    public void setEndDate(int year, int month, int day) {
        selectEndYearMonth = year + "年" + month + "月";
        selectEndDay = day + "";
        issetdata = true;
        if (year == getYear()) {
            this.month = getMonth();
        } else {
            this.month = 12;
        }
        calDays(year, month);
    }

    public void calDays(int year, int month) {
        boolean leayyear = false;
        leayyear = year % 4 == 0 && year % 100 != 0;
        for (int i = 1; i <= 12; i++) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    this.day = 31;
                    break;
                case 2:
                    if (leayyear) {
                        this.day = 29;
                    } else {
                        this.day = 28;
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    this.day = 30;
                    break;
            }
        }
//        if (year == getYear() && month == getMonth()) {
//            this.day = getDay();
//        }
    }
}