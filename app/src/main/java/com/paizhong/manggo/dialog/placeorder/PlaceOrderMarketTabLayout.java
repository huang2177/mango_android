package com.paizhong.manggo.dialog.placeorder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.ui.kchart.bean.KlineCycle;

import java.util.List;

/**
 * Created by zab on 2018/7/13 0013.
 */
public class PlaceOrderMarketTabLayout extends HorizontalScrollView implements View.OnClickListener {

    private TextView m1TabName;
    private View m1TabLine;

    private TextView m5TabName;
    private View m5TabLine;

    private TextView m15TabName;
    private View m15TabLine;

    private TextView m30TabName;
    private View m30TabLine;

    private TextView h1TabName;
    private View h1TabLine;

    private TextView h4TabName;
    private View h4TabLine;

    private TextView dTabName;
    private View dTabLine;

    private TextView wTabName;
    private View wTabLine;

    public PlaceOrderMarketTabLayout(Context context) {
        super(context);
    }

    public PlaceOrderMarketTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaceOrderMarketTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //1分钟
        View m1TabView = findViewById(R.id.common_tab1);
        m1TabName = (TextView) findViewById(R.id.common_tab_name1);
        m1TabLine = findViewById(R.id.common_tab_line1);
        //5分钟
        View m5TabView = findViewById(R.id.common_tab2);
        m5TabName = (TextView) findViewById(R.id.common_tab_name2);
        m5TabLine = findViewById(R.id.common_tab_line2);
        //15分钟
        View m15TabView = findViewById(R.id.common_tab3);
        m15TabName = (TextView) findViewById(R.id.common_tab_name3);
        m15TabLine = findViewById(R.id.common_tab_line3);
        //30分钟
        View m30TabView = findViewById(R.id.common_tab4);
        m30TabName = (TextView) findViewById(R.id.common_tab_name4);
        m30TabLine = findViewById(R.id.common_tab_line4);
        //1小时
        View h1TabView = findViewById(R.id.common_tab5);
        h1TabName = (TextView) findViewById(R.id.common_tab_name5);
        h1TabLine = findViewById(R.id.common_tab_line5);
        //4小时
        View h4TabView = findViewById(R.id.common_tab6);
        h4TabName = (TextView) findViewById(R.id.common_tab_name6);
        h4TabLine = findViewById(R.id.common_tab_line6);
        //日线
        View dTabView = findViewById(R.id.common_tab7);
        dTabName = (TextView) findViewById(R.id.common_tab_name7);
        dTabLine = findViewById(R.id.common_tab_line7);
        //周线
        View wTabView = findViewById(R.id.common_tab8);
        wTabName = (TextView) findViewById(R.id.common_tab_name8);
        wTabLine = findViewById(R.id.common_tab_line8);


        m1TabView.setOnClickListener(this);
        m5TabView.setOnClickListener(this);
        m15TabView.setOnClickListener(this);
        m30TabView.setOnClickListener(this);
        h1TabView.setOnClickListener(this);
        h4TabView.setOnClickListener(this);
        dTabView.setOnClickListener(this);
        wTabView.setOnClickListener(this);
    }

    private List<KlineCycle> mKlineCycleList;
    public void replaceTabCodes(List<KlineCycle> klineCycleList){
        this.mKlineCycleList = klineCycleList;
    }

    private TabSelectListener selectListener;
    public void setTabSelectListener(TabSelectListener listener){
        this.selectListener = listener;
    }
    public interface TabSelectListener{
        void onTabSelect(String code, String timeCode);
    }

    public void initSelectorTab(){
        if (!m1TabName.isSelected()){
            selectorTabStyle(R.id.common_tab1);
        }
        if (selectListener !=null && mKlineCycleList !=null && mKlineCycleList.size() > 0){
            selectListener.onTabSelect(mKlineCycleList.get(0).getCode(),mKlineCycleList.get(0).getTimeCode());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_tab1: //分时
                if (!m1TabName.isSelected()){
                    selectorTabStyle(R.id.common_tab1);
                    if (selectListener !=null && mKlineCycleList !=null && mKlineCycleList.size() > 0){
                        selectListener.onTabSelect(mKlineCycleList.get(0).getCode(),mKlineCycleList.get(0).getTimeCode());
                    }
                }
                break;
            case R.id.common_tab8: //1分线
                if (!wTabName.isSelected()){
                    selectorTabStyle(R.id.common_tab8);
                    if (selectListener !=null && mKlineCycleList !=null && mKlineCycleList.size() > 1){
                        selectListener.onTabSelect(mKlineCycleList.get(1).getCode(),mKlineCycleList.get(1).getTimeCode());
                    }
                }
                break;
            case R.id.common_tab2: //5分钟
                if (!m5TabName.isSelected()){
                    selectorTabStyle(R.id.common_tab2);
                    if (selectListener !=null && mKlineCycleList !=null && mKlineCycleList.size() > 2){
                        selectListener.onTabSelect(mKlineCycleList.get(2).getCode(),mKlineCycleList.get(2).getTimeCode());
                    }
                }
                break;
            case R.id.common_tab3: //15分钟
                if (!m15TabName.isSelected()){
                    selectorTabStyle(R.id.common_tab3);
                    if (selectListener !=null && mKlineCycleList !=null && mKlineCycleList.size() > 3){
                        selectListener.onTabSelect(mKlineCycleList.get(3).getCode(),mKlineCycleList.get(3).getTimeCode());
                    }
                }
                break;
            case R.id.common_tab4: //30分钟
                if (!m30TabName.isSelected()){
                    selectorTabStyle(R.id.common_tab4);
                    if (selectListener !=null && mKlineCycleList !=null && mKlineCycleList.size() > 4){
                        selectListener.onTabSelect(mKlineCycleList.get(4).getCode(),mKlineCycleList.get(4).getTimeCode());
                    }
                }
                break;
            case R.id.common_tab5: //1小时
                if (!h1TabName.isSelected()){
                    selectorTabStyle(R.id.common_tab5);
                    if (selectListener !=null && mKlineCycleList !=null && mKlineCycleList.size() > 5){
                        selectListener.onTabSelect(mKlineCycleList.get(5).getCode(),mKlineCycleList.get(5).getTimeCode());
                    }
                }
                break;
            case R.id.common_tab6: //4小时
                if (!h4TabName.isSelected()){
                    selectorTabStyle(R.id.common_tab6);
                    if (selectListener !=null && mKlineCycleList !=null && mKlineCycleList.size() > 6){
                        selectListener.onTabSelect(mKlineCycleList.get(6).getCode(),mKlineCycleList.get(6).getTimeCode());
                    }
                }
                break;
            case R.id.common_tab7: //日线
                if (!dTabName.isSelected()){
                    selectorTabStyle(R.id.common_tab7);
                    if (selectListener !=null && mKlineCycleList !=null && mKlineCycleList.size() > 7){
                        selectListener.onTabSelect(mKlineCycleList.get(7).getCode(),mKlineCycleList.get(7).getTimeCode());
                    }
                }
                break;
        }
    }

    public void selectorTabStyle(int resID){
        if (m1TabName.isSelected()){
            m1TabName.setSelected(false);
            m1TabLine.setVisibility(INVISIBLE);
        }
        if (m5TabName.isSelected()){
            m5TabName.setSelected(false);
            m5TabLine.setVisibility(INVISIBLE);
        }
        if (m15TabName.isSelected()){
            m15TabName.setSelected(false);
            m15TabLine.setVisibility(INVISIBLE);
        }
        if (m30TabName.isSelected()){
            m30TabName.setSelected(false);
            m30TabLine.setVisibility(INVISIBLE);
        }
        if (h1TabName.isSelected()){
            h1TabName.setSelected(false);
            h1TabLine.setVisibility(INVISIBLE);
        }
        if (h4TabName.isSelected()){
            h4TabName.setSelected(false);
            h4TabLine.setVisibility(INVISIBLE);
        }
        if (dTabName.isSelected()){
            dTabName.setSelected(false);
            dTabLine.setVisibility(INVISIBLE);
        }
        if (wTabName.isSelected()){
            wTabName.setSelected(false);
            wTabLine.setVisibility(INVISIBLE);
        }
        switch (resID) {
            case R.id.common_tab1: //分时
                m1TabName.setSelected(true);
                m1TabLine.setVisibility(VISIBLE);
                break;
            case R.id.common_tab2: //5分钟
                m5TabName.setSelected(true);
                m5TabLine.setVisibility(VISIBLE);
                break;
            case R.id.common_tab3: //15分钟
                m15TabName.setSelected(true);
                m15TabLine.setVisibility(VISIBLE);
                break;
            case R.id.common_tab4: //30分钟
                m30TabName.setSelected(true);
                m30TabLine.setVisibility(VISIBLE);
                break;
            case R.id.common_tab5: //1小时
                h1TabName.setSelected(true);
                h1TabLine.setVisibility(VISIBLE);
                break;
            case R.id.common_tab6: //4小时
                h4TabName.setSelected(true);
                h4TabLine.setVisibility(VISIBLE);
                break;
            case R.id.common_tab7: //日线
                dTabName.setSelected(true);
                dTabLine.setVisibility(VISIBLE);
                break;
            case R.id.common_tab8: //周线
                wTabName.setSelected(true);
                wTabLine.setVisibility(VISIBLE);
                break;
        }
    }
}
