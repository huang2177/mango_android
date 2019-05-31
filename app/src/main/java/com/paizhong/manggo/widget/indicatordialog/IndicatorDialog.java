package com.paizhong.manggo.widget.indicatordialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.paizhong.manggo.R;
import com.paizhong.manggo.utils.DeviceUtils;


/**
 * Des: 带有指示器的dialog
 * Created by hs on 2018/5/28 0028 15:13
 */

public class IndicatorDialog {

    private static final String TAG = "HIndicatorDialog";
    public static float ARROW_RECTAGE = 0.1f;
    private int gravity = Gravity.TOP | Gravity.LEFT;
    /**
     * 控件
     */
    private Activity mContext;
    private Dialog mDialog;
    private IndicatorBuilder mBuilder;
    private RecyclerView recyclerView;
    private LinearLayout rootLayout;
    private View mArrow;
    private CardView mCardView;
    private View mShowView;
    /**
     * 变量
     */
    private int mArrowWidth;
    private int mWidth;
    private int mResultHeight;


    public IndicatorDialog(Activity context, IndicatorBuilder builder) {
        this.mContext = context;
        this.mBuilder = builder;
        if (mBuilder.mArrowWidth <= 0) {
            this.mArrowWidth = (int) (mBuilder.width * ARROW_RECTAGE);
        } else {
            this.mArrowWidth = mBuilder.mArrowWidth;
        }
        initDialog();
    }

    private void initDialog() {
        mDialog = new Dialog(mContext, R.style.H_Dialog_Style);

        if (mDialog.getWindow() != null) {
            mDialog.getWindow().setDimAmount(mBuilder.alpha);
        }

        rootLayout = new LinearLayout(mContext);
        if (mBuilder.arrowdirection == IndicatorBuilder.TOP || mBuilder.arrowdirection == IndicatorBuilder.BOTTOM) {
            rootLayout.setOrientation(LinearLayout.VERTICAL);
            this.mWidth = mBuilder.width;
        } else {
            rootLayout.setOrientation(LinearLayout.HORIZONTAL);
            this.mWidth = mBuilder.width + mArrowWidth;
        }
        ViewGroup.LayoutParams rootParam = new ViewGroup.LayoutParams(mWidth,
                mBuilder.height <= 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : mBuilder.height);
        rootLayout.setLayoutParams(rootParam);
        if (mBuilder.arrowdirection == IndicatorBuilder.TOP || mBuilder.arrowdirection == IndicatorBuilder.LEFT) {
            addArrow2LinearLayout();
        }

        addRecyclerView2Layout();

        if (mBuilder.arrowdirection == IndicatorBuilder.BOTTOM || mBuilder.arrowdirection == IndicatorBuilder.RIGHT) {
            addArrow2LinearLayout();
        }

        mDialog.setContentView(rootLayout);

        setSize2Dialog(mBuilder.height);

        addShadowView();

        mDialog.setOnDismissListener(mBuilder.dismissListener);
        mDialog.setCanceledOnTouchOutside(mBuilder.enableTouchOutside);
    }

    /**
     * 添加可点击阴影
     */
    public void addShadowView() {
        if (mBuilder.enableDismissView) {
            View dismissView = new View(mContext);
            dismissView.setAlpha(0.5f);
            dismissView.setBackgroundColor(Color.BLACK);
            mBuilder.mAdapter.clearFooterView();
            mBuilder.mAdapter.addFooterView(dismissView, ViewGroup.LayoutParams.MATCH_PARENT);
            dismissView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
    }

    private void addArrow2LinearLayout() {
        mArrow = new View(mContext);
        rootLayout.addView(mArrow);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mArrow.getLayoutParams();
        layoutParams.width = mArrowWidth;
        layoutParams.height = mArrowWidth;
        mArrow.setLayoutParams(layoutParams);
    }

    /**
     * modify recyclerview state
     */
    private void addRecyclerView2Layout() {
        mCardView = (CardView) LayoutInflater.from(mContext).inflate(R.layout.indicator_dialog_layout, null);
//        mCardView = (CardView) childLayout.findViewById(R.id.dialog_cv);
        rootLayout.addView(mCardView);
        recyclerView = (RecyclerView) mCardView.findViewById(R.id.dialog_rv);

        mCardView.setRadius(mBuilder.radius);
        mCardView.setCardBackgroundColor(mBuilder.bgColor);
        CardView.LayoutParams layoutParams = (CardView.LayoutParams) recyclerView.getLayoutParams();
        int width = mBuilder.width;
        if (mBuilder.arrowdirection == IndicatorBuilder.RIGHT) {
            width -= mArrowWidth;
        }
        layoutParams.width = width;
        recyclerView.setLayoutParams(layoutParams);
        recyclerView.setBackgroundColor(mBuilder.bgColor);
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int recyclerViewHeight = recyclerView.getHeight();
                resizeHeight(recyclerViewHeight);
            }
        });
    }

    private void setSize2Dialog(int height) {
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        if (mBuilder.animator != 0) {
            dialogWindow.setWindowAnimations(mBuilder.animator);
        }
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        if (mBuilder.gravity == IndicatorBuilder.GRAVITY_RIGHT) {
            gravity = Gravity.RIGHT | (mBuilder.arrowdirection != IndicatorBuilder.BOTTOM ? Gravity.TOP : Gravity.BOTTOM);
        } else if (mBuilder.gravity == IndicatorBuilder.GRAVITY_LEFT) {
            gravity = Gravity.LEFT | (mBuilder.arrowdirection != IndicatorBuilder.BOTTOM ? Gravity.TOP : Gravity.BOTTOM);
        } else {
            gravity = Gravity.CENTER_HORIZONTAL | (mBuilder.arrowdirection != IndicatorBuilder.BOTTOM ? Gravity.TOP : Gravity.BOTTOM);
        }
        dialogWindow.setGravity(gravity);
        lp.width = mBuilder.width; // 宽度
        lp.height = height; // 高度
        dialogWindow.setAttributes(lp);
    }

    public void resizeHeight(int recyclerViewHeight) {
        int arrowHeight = mBuilder.arrowdirection == IndicatorBuilder.TOP || mBuilder.arrowdirection == IndicatorBuilder.BOTTOM ? mArrowWidth : 0;
        int calcuteResult = recyclerViewHeight + arrowHeight;
        int recyc_height = recyclerViewHeight;
        if (mBuilder.height <= 0 || calcuteResult < mBuilder.height) {
            mResultHeight = calcuteResult;
        } else {
            mResultHeight = mBuilder.height;
            if (mBuilder.arrowdirection == IndicatorBuilder.BOTTOM) {
                recyc_height = mResultHeight - mArrowWidth;
            }
        }

        ViewGroup.LayoutParams params = rootLayout.getLayoutParams();

        //重新获取屏幕能接受的最大高度，如果当前高度超过屏幕能接受的恩最大高度，则设置为屏幕能接受的高度
        int canUseHeight = 0;
        if (mShowView != null) {
            int[] location = new int[2];
            mShowView.getLocationInWindow(location);

            int windowHeight = DeviceUtils.getLocationInWindow(mContext)[1];
            if (mBuilder.arrowdirection == IndicatorBuilder.TOP) {
                canUseHeight = windowHeight - location[1] - mShowView.getHeight();
            } else if (mBuilder.arrowdirection == IndicatorBuilder.BOTTOM) {
                canUseHeight = location[1] - DeviceUtils.getStatusBarHeight(mContext);
            } else {
                canUseHeight = DeviceUtils.getLocationInContent(mContext)[1];
            }
        }

        if (mResultHeight > canUseHeight) {
            mResultHeight = canUseHeight;
            if (mBuilder.arrowdirection == IndicatorBuilder.BOTTOM) {
                recyc_height = mResultHeight - mArrowWidth;
            }
        }
        params.height = mResultHeight;
        rootLayout.setLayoutParams(params);


        if (mBuilder.arrowdirection == IndicatorBuilder.BOTTOM) {
            ViewGroup.LayoutParams recyc_layoutParam = recyclerView.getLayoutParams();
            recyc_layoutParam.height = recyc_height;
            recyclerView.setLayoutParams(recyc_layoutParam);
        }

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mArrow.getLayoutParams();
        if (mBuilder.arrowdirection == IndicatorBuilder.TOP) {
            layoutParams.leftMargin = (int) (mBuilder.width * mBuilder.arrowPercentage) - mArrowWidth / 2;
        } else if (mBuilder.arrowdirection == IndicatorBuilder.BOTTOM) {
            layoutParams.leftMargin = (int) (mBuilder.width * mBuilder.arrowPercentage) - mArrowWidth / 2;
        } else {
            layoutParams.topMargin = (int) (mResultHeight * mBuilder.arrowPercentage) - mArrowWidth / 2;
            Window dialogWindow = mDialog.getWindow();
            dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.y = lp.y - ((int) (mResultHeight * mBuilder.arrowPercentage));
            dialogWindow.setAttributes(lp);
        }
        mArrow.setLayoutParams(layoutParams);
        BaseDrawable arrowDrawable;
        if (mBuilder.mArrowDrawable == null) {
            arrowDrawable = new TriangleDrawable(mBuilder.arrowdirection, mBuilder.bgColor);
        } else {
            arrowDrawable = mBuilder.mArrowDrawable;
        }
        arrowDrawable.setBounds(mArrow.getLeft(), mArrow.getTop(), mArrow.getRight(), mArrow.getBottom());
        mArrow.setBackgroundDrawable(arrowDrawable);
        rootLayout.requestLayout();
        setSize2Dialog(mResultHeight);

    }

    public static IndicatorDialog newInstance(Activity context, IndicatorBuilder builder) {
        return new IndicatorDialog(context, builder);
    }

    public void show(View view) {
        mShowView = view;
        int x = 0;
        int y = 0;
        if (mBuilder.arrowdirection == IndicatorBuilder.TOP || mBuilder.arrowdirection == IndicatorBuilder.BOTTOM) {
            if (mBuilder.gravity == IndicatorBuilder.GRAVITY_LEFT) {
                x = -1 * (int) (mBuilder.width * mBuilder.arrowPercentage) + view.getWidth() / 2;
            } else if (mBuilder.gravity == IndicatorBuilder.GRAVITY_RIGHT) {
                x = -1 * (mBuilder.width - (int) (mBuilder.width * mBuilder.arrowPercentage)) + view.getWidth() / 2;
            }
        } else {
            if (mBuilder.gravity == IndicatorBuilder.GRAVITY_LEFT) {
                x = view.getWidth() - mArrowWidth / 2;
            } else if (mBuilder.gravity == IndicatorBuilder.GRAVITY_RIGHT) {
                x = view.getWidth() - mArrowWidth / 2;
            }

        }
        show(view, x, y);
    }

    public void show(View view, int xOffset, int yOffset) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        int height = DeviceUtils.getLocationInWindow(mContext)[1];
        int width = DeviceUtils.getLocationInWindow(mContext)[0];

        int x;
        int y;
        if ((gravity & Gravity.RIGHT) == Gravity.RIGHT) {
            x = width - location[0] - view.getWidth();
        } else if ((gravity & Gravity.LEFT) == Gravity.LEFT) {
            x = location[0];
        } else {
            x = 0;
        }
        x += xOffset;
        if (x < 0) {
            x = 0;
        }
        if (mBuilder.arrowdirection == IndicatorBuilder.BOTTOM) {
            y = height - location[1] + DeviceUtils.getNavigationBarHeight(mContext) - mArrowWidth / 2;
        } else if (mBuilder.arrowdirection == IndicatorBuilder.TOP) {
            y = location[1] + view.getHeight() - mArrowWidth / 2;
        } else {
            y = location[1] + view.getHeight() / 2;
        }
        y += yOffset;
        if (y < 0) {
            y = 0;
        }
        show(x, y);
    }

    public void show(int x, int y) {
        recyclerView.setLayoutManager(mBuilder.mLayoutManager);
        recyclerView.setAdapter(mBuilder.mAdapter);
        setDialogPosition(x, y);
        mDialog.show();
    }

    private void setDialogPosition(int x, int y) {
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = x;
        lp.y = y;
        dialogWindow.setAttributes(lp);

    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public Dialog getDialog() {
        return mDialog;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public CardView getCardView() {
        return mCardView;
    }

    public View getArrow() {
        return mArrow;
    }
}
