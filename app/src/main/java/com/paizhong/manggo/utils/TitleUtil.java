package com.paizhong.manggo.utils;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.widget.DrawableCenterTextView;


public class TitleUtil {
    private Toolbar mToolbar;
    private DrawableCenterTextView mLeft;
    private TextView mTitle;
    private DrawableCenterTextView mRight;
    private AppCompatActivity mActivity;

    //activity构造
    public TitleUtil(AppCompatActivity activity, View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (mToolbar == null) {
            return;
        }
        mLeft = (DrawableCenterTextView) view.findViewById(R.id.tv_left);
        mTitle = (TextView) view.findViewById(R.id.tv_title);
        mRight = (DrawableCenterTextView) view.findViewById(R.id.tv_right);
        this.mActivity = activity;
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void setToolBarHeight(int height) {
        ViewGroup.LayoutParams layoutParams = mToolbar.getLayoutParams();
        layoutParams.height = mActivity.getResources().getDimensionPixelSize(height);
        mToolbar.setLayoutParams(layoutParams);
    }

    public void setTitleVisibility(int visibility) {
        mToolbar.setVisibility(visibility);
    }


    public void setTitle(boolean isShowBack, String title) {
        setTitle(isShowBack, 0, 0, 0, title, null);
    }

    public void setTitle(boolean isShowBack, int bgColor, int textColor, String title) {
        setTitle(isShowBack, 0, bgColor, textColor, title, null);
    }

    public void setTitle(boolean isShowBack, int backImgId, String title, View.OnClickListener listener) {
        setTitle(isShowBack, backImgId, 0, 0, title, listener);
    }

    public void setTitle(boolean isShowBack, int backImgId, int bgColor, int textColor, String title, View.OnClickListener listener) {
        if (bgColor != 0) {
            mToolbar.setBackgroundColor(ContextCompat.getColor(mActivity, bgColor));
        }
        if (textColor != 0) {
            int color = ContextCompat.getColor(mActivity, textColor);
            mTitle.setTextColor(color);
            mRight.setTextColor(color);
        }
        mTitle.setText(title);
        if (isShowBack) {
            Drawable left = null;
            if (backImgId == 0) {
                left = ContextCompat.getDrawable(mActivity, R.mipmap.ic_left_arrow);
            } else {
                left = ContextCompat.getDrawable(mActivity, backImgId);
            }
            left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
            mLeft.setCompoundDrawables(left, null, null, null);
            if (listener == null) {
                mLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.finish();
                    }
                });
            } else {
                mLeft.setOnClickListener(listener);
            }
        }
    }


    public void setRightTitle(int backImgId, int textColor, View.OnClickListener listener) {
        setRightTitle(null, textColor, backImgId, listener);
    }

    public void setRightTitle(String rightTitle, int textColor, View.OnClickListener listener) {
        setRightTitle(rightTitle, textColor, 0, listener);
    }

    public void setRightTitle(String rightTitle, int textColor, int backImgId, View.OnClickListener listener) {
        if (listener !=null){
            mRight.setOnClickListener(listener);
        }
        if (backImgId != 0) {
            Drawable right = ContextCompat.getDrawable(mActivity, backImgId);
            right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
            mRight.setCompoundDrawables(null, null, right, null);
        }

        if (rightTitle != null) {
            mRight.setText(rightTitle);
        }
        if (textColor != 0) {
            mRight.setTextColor(ContextCompat.getColor(mActivity, textColor));
        }
    }


    public void setRightBgTitle(String rightTitle, int textColor, int bgImgId, View.OnClickListener listener) {
        mRight.setOnClickListener(listener);

        if (bgImgId != 0) {
            Drawable right = ContextCompat.getDrawable(mActivity, bgImgId);
            mRight.setBackground(right);
            Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) mRight.getLayoutParams();
            int dimensionPixelSize = mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_10dp);
            layoutParams.topMargin = dimensionPixelSize;
            layoutParams.bottomMargin = dimensionPixelSize;
            layoutParams.rightMargin = mActivity.getResources().getDimensionPixelSize(R.dimen.app_margin);
            mRight.setLayoutParams(layoutParams);
            mRight.setPadding(mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_18dp)
                    , mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_2dp)
                    , mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_18dp)
                    , mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_2dp));
        }

        if (rightTitle != null) {
            mRight.setText(rightTitle);
        }
        if (textColor != 0) {
            mRight.setTextColor(ContextCompat.getColor(mActivity, textColor));
        }
    }
}
