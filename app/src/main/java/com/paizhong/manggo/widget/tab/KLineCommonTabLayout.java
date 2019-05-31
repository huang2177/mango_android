package com.paizhong.manggo.widget.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
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

public class KLineCommonTabLayout extends HorizontalScrollView {

    private Context mContext;
    private LinearLayout mTabsContainer;

    public int mTabCount;
    private int mCurrentTab;
    private List<KlineCycle> commonTabList = new ArrayList<>();

    public KLineCommonTabLayout(Context context) {
        this(context,null,0);
    }

    public KLineCommonTabLayout(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public KLineCommonTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);//重写onDraw方法,需要调用这个方法来清除flag
        setClipChildren(false);
        setClipToPadding(false);

        this.mContext = context;
        this.mTabsContainer = new LinearLayout(context);
        TypedArray typedArray = attrs == null ? null : context.obtainStyledAttributes(attrs, R.styleable.KLineCommonTabLayout);
        if (typedArray !=null){
            boolean yeStyle = typedArray.getBoolean(R.styleable.KLineCommonTabLayout_kLineCommonTabYeStyle, true);
            if (yeStyle){
                this.mSelectColor  = AppApplication.getConfig().getYeStyle() ? ContextCompat.getColor(mContext,R.color.color_ffffff) : ContextCompat.getColor(mContext,R.color.color_323232);
                this.mNoSelectColor  = AppApplication.getConfig().getYeStyle() ? ContextCompat.getColor(mContext,R.color.color_ffffff) : ContextCompat.getColor(mContext,R.color.color_878787);
            }else {
                this.mSelectColor  = ContextCompat.getColor(mContext,R.color.color_323232);
                this.mNoSelectColor  = ContextCompat.getColor(mContext,R.color.color_878787);
            }
            typedArray.recycle();
        }else {
            this.mSelectColor  = AppApplication.getConfig().getYeStyle() ? ContextCompat.getColor(mContext,R.color.color_ffffff) : ContextCompat.getColor(mContext,R.color.color_323232);
            this.mNoSelectColor  = AppApplication.getConfig().getYeStyle() ? ContextCompat.getColor(mContext,R.color.color_ffffff) : ContextCompat.getColor(mContext,R.color.color_878787);
        }
        addView(mTabsContainer);
    }

    public void addTab(List<KlineCycle> list){
        if (list == null || list.size() <= 0){return;}
        this.commonTabList.clear();
        this.commonTabList.addAll(list);
        this.mTabCount = list.size();
        for (int i = 0;i < list.size() ; i ++){
            LinearLayout view = (LinearLayout) View.inflate(mContext, R.layout.layout_kline_common_tab,null);
            TextView tvName = (TextView)view.findViewById(R.id.common_tab_name);
            View vLine = view.findViewById(R.id.common_tab_line);

            KlineCycle bean = list.get(i);
            if (!TextUtils.isEmpty(bean.getName()) && !TextUtils.isEmpty(bean.getCode())){
                tvName.setText(bean.getName());
                if (i == 0){
                    vLine.setVisibility(VISIBLE);
                    tvName.setTextColor(AppApplication.getConfig().getYeStyle() ? mSelectColor : mNoSelectColor);
                }else {
                    tvName.setTextColor(AppApplication.getConfig().getYeStyle() ? mSelectColor : mNoSelectColor);
                    vLine.setVisibility(INVISIBLE);
                }
                if (i == 0){
                    view.setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.dimen_15dp),0,mContext.getResources().getDimensionPixelSize(R.dimen.dimen_32dp),0);
                }else if (i == list.size()-1){
                    view.setPadding(0,0,mContext.getResources().getDimensionPixelSize(R.dimen.dimen_15dp),0);
                }else {
                    view.setPadding(0,0,mContext.getResources().getDimensionPixelSize(R.dimen.dimen_32dp),0);
                }

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

                mTabsContainer.addView(view);
            }
        }

//        if (mListener !=null && commonTabList !=null && commonTabList.size() > 0){
//            String type = commonTabList.get(0).getCode();
//            mListener.onTabSelect(0,type);
//        }
    }

    private int mSelectColor;
    private int mNoSelectColor;
    public void updateTabSelection(int position){
        mCurrentTab = position;
        for (int i = 0 ;i < mTabCount ; i ++){
            View tabView = mTabsContainer.getChildAt(i);
            final boolean isSelect = i == position;

            TextView tvName = (TextView)tabView.findViewById(R.id.common_tab_name);
            tvName.setTextColor(isSelect ? mSelectColor : mNoSelectColor);
            View vLine = tabView.findViewById(R.id.common_tab_line);
            vLine.setVisibility(isSelect ? VISIBLE : INVISIBLE);
        }
    }

    public void updateTabStyle(boolean mYeSyle){
      if (AppApplication.getConfig().getYeStyle() != mYeSyle && mTabCount > 0){
          this.mSelectColor  = mYeSyle ? ContextCompat.getColor(mContext,R.color.color_ffffff) : ContextCompat.getColor(mContext,R.color.color_333333);
          this.mNoSelectColor  = mYeSyle ? ContextCompat.getColor(mContext,R.color.color_ffffff) : ContextCompat.getColor(mContext,R.color.color_878787);
          updateTabSelection(mCurrentTab);
      }
    }

    private OnMyTabSelectListener mListener;

    public void setOnMyTabSelectListener(OnMyTabSelectListener listener) {
        this.mListener = listener;
    }
}
