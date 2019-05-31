package com.paizhong.manggo.widget.tab;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.ui.kchart.bean.KlineCycle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zab on 2018/3/30 0030.
 */

public class KLineCommonNrTabLayout extends LinearLayout {

    private Context mContext;

    public int mTabCount;
    private int mCurrentTab;
    private List<KlineCycle> commonTabList = new ArrayList<>();

    public KLineCommonNrTabLayout(Context context) {
        this(context,null,0);
    }

    public KLineCommonNrTabLayout(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public KLineCommonNrTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);//重写onDraw方法,需要调用这个方法来清除flag
        setClipChildren(false);
        setClipToPadding(false);

        this.mContext = context;
        this.mSelectColor  = AppApplication.getConfig().getYeStyle() ? ContextCompat.getColor(mContext,R.color.color_ffffff) : ContextCompat.getColor(mContext,R.color.color_323232);
        this.mNoSelectColor  = AppApplication.getConfig().getYeStyle() ? ContextCompat.getColor(mContext,R.color.color_ffffff) : ContextCompat.getColor(mContext,R.color.color_878787);
        this.mBgColor = AppApplication.getConfig().getYeStyle() ? ContextCompat.getColor(mContext,R.color.color_1F1C28) : ContextCompat.getColor(mContext,R.color.color_F2F2F2);
    }

    public void addTab(List<KlineCycle> list){
        if (list == null || list.size() <= 0){return;}
        this.commonTabList.clear();
        this.commonTabList.addAll(list);
        this.mTabCount = list.size();
        for (int i = 0;i < list.size() ; i ++){
            LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.layout_kline_common_nr_tab,null);
            TextView tvName = (TextView)view.findViewById(R.id.common_tab_name);

            KlineCycle bean = list.get(i);
            if (!TextUtils.isEmpty(bean.getName()) && !TextUtils.isEmpty(bean.getCode())){
                tvName.setText(bean.getName());
                if (i == 0){
                    tvName.setTextColor(mSelectColor);
                    view.setBackgroundColor(mBgColor);
                }else {
                    tvName.setTextColor( mNoSelectColor);
                    view.setBackgroundColor(0);
                }
                LayoutParams lp_tab = new LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                view.setLayoutParams(lp_tab);

                view.setTag(i);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        if (mCurrentTab != position) {
                            updateTabSelection(position);
                            if (mListener !=null){

                                String type = null;
                                if (commonTabList !=null && commonTabList.size() > position){
                                    type = commonTabList.get(position).getCode();
                                }
                                mListener.onTabSelect(position,type);
                            }
                        } else {

                        }
                    }
                });

                addView(view);
            }
        }

//        if (mListener !=null && commonTabList !=null && commonTabList.size() > 0){
//            String type = commonTabList.get(0).getCode();
//            mListener.onTabSelect(0,type);
//        }
    }

    private int mSelectColor;
    private int mNoSelectColor;
    private int mBgColor;
    public void updateTabSelection(int position){
        mCurrentTab = position;
        for (int i = 0 ;i < mTabCount ; i ++){
            View tabView = getChildAt(i);
            final boolean isSelect = i == position;
            TextView tvName = tabView.findViewById(R.id.common_tab_name);
            tvName.setTextColor(isSelect ? mSelectColor : mNoSelectColor);
            tabView.setBackgroundColor(isSelect ? mBgColor : 0);
        }
    }

    public void updateTabStyle(boolean mYeSyle){
      if (AppApplication.getConfig().getYeStyle() != mYeSyle && mTabCount > 0){
          this.mSelectColor  = mYeSyle ? ContextCompat.getColor(mContext,R.color.color_ffffff) : ContextCompat.getColor(mContext,R.color.color_333333);
          this.mNoSelectColor  = mYeSyle ? ContextCompat.getColor(mContext,R.color.color_ffffff) : ContextCompat.getColor(mContext,R.color.color_878787);
          this.mBgColor = mYeSyle ? ContextCompat.getColor(mContext,R.color.color_1F1C28) : ContextCompat.getColor(mContext,R.color.color_F2F2F2);
          updateTabSelection(mCurrentTab);
      }
    }

    private OnMyTabSelectListener mListener;

    public void setOnMyTabSelectListener(OnMyTabSelectListener listener) {
        this.mListener = listener;
    }
}
