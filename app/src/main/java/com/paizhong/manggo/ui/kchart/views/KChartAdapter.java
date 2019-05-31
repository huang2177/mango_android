package com.paizhong.manggo.ui.kchart.views;

import android.text.TextUtils;

import com.paizhong.manggo.ui.kchart.bean.KLineEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据适配器
 */
public class KChartAdapter extends BaseKChartAdapter {

    private List<KLineEntity> datas = new ArrayList<>();

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public String getDate(int position) {
        String time = datas.get(position).dateStr;
        return TextUtils.isEmpty(time) ? "" : time;
    }

    /**
     * 向头部添加数据
     *
     * @param data
     */
    public void addHeaderData(List<KLineEntity> data) {
        if (data != null && !data.isEmpty()) {
            datas.addAll(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 向尾部添加数据
     *
     * @param data
     */
    public void addFooterData(List<KLineEntity> data) {
        if (data != null && !data.isEmpty()) {
            datas.addAll(0, data);
            notifyDataSetChanged();
        }
    }

    public void setData(List<KLineEntity> data) {
        if (data != null && !data.isEmpty()) {
            datas.clear();
            datas.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        datas.clear();
        notifyDataSetChanged();
    }
}
