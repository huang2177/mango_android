package com.paizhong.manggo.bean.follow;

import com.github.mikephil.charting.data.PieEntry;

import java.util.List;

/**
 * Des:
 * Created by huang on 2018/9/12 0012 18:38
 */
public class WeekCountEntry {
    public String proName;
    public String winCount;
    public String lossCount;
    public List<PieEntry> map;

    public WeekCountEntry(String proName, String winCount, String lossCount, List<PieEntry> map) {
        this.proName = proName;
        this.winCount = winCount;
        this.lossCount = lossCount;
        this.map = map;
    }
}
