package com.paizhong.manggo.ui.kchart.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;


import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.ui.kchart.bean.KLineBean;
import com.paizhong.manggo.ui.kchart.bean.KLineEntity;
import com.paizhong.manggo.ui.kchart.formatter.IValueFormatter;
import com.paizhong.manggo.ui.kchart.formatter.ValueFormatter;
import com.paizhong.manggo.ui.kchart.internal.Candle;
import com.paizhong.manggo.ui.kchart.internal.IAdapter;
import com.paizhong.manggo.ui.kchart.internal.IChartDraw;
import com.paizhong.manggo.ui.kchart.internal.IKChartView;
import com.paizhong.manggo.ui.kchart.internal.KLine;
import com.paizhong.manggo.ui.kchart.utils.KLineChartController;
import com.paizhong.manggo.ui.kchart.utils.KLineUtils;
import com.paizhong.manggo.ui.kchart.utils.MinuteHourLineUtils;
import com.paizhong.manggo.ui.kchart.utils.ViewUtil;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.widget.tab.CommonTabLayout;
import com.paizhong.manggo.widget.tab.CustomTabEntity;
import com.paizhong.manggo.widget.tab.SimpleOnTabSelectListener;
import com.paizhong.manggo.widget.tab.TabEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * k线图
 */
public abstract class BaseKChart extends ScrollAndScaleView
        implements IKChartView, IKChartView.OnSelectedChangedListener {

    protected int mMainDrawPosition = 0;
    protected int mChildDrawPosition = 0;
    //x轴的偏移量
    //protected float mTranslateX = Float.MIN_VALUE;
    protected float mTranslateX = -150;
    //k线图形区域的高度
    protected int mMainHeight = 0;
    //图形区域的宽度
    protected int mWidth = 0;
    //下方子图的高度
    protected int mChildHeight = 0;
    //图像上padding
    protected int mTopPadding = 0;
    //初始化距离右边距离
    protected float mRightInitPadding = 0;
    //图像下padding
    protected int mBottomPadding = 0;
    //k线图距离顶部的距离
    protected int mMainTopSpace = 30;
    //k线图和子图像之间的空隙
    protected int mMainChildSpace = 30;
    //k线图和时间之间的空隙
    protected int mMainTimeSpace = 18;
    //k线图y轴的缩放
    protected float mMainScaleY = 1;
    //子图轴的缩放
    protected float mChildScaleY = 1;
    //数据的真实长度
    protected int mDataLen = 0;
    //k线图当前显示区域的最大值
    protected float mMainMaxValue = Float.MAX_VALUE;
    //k线图当前显示区域的最大值 的下标
    protected int mMainMaxVaueIndex = -1;
    //k线图当前显示区域的最小值
    protected float mMainMinValue = Float.MIN_VALUE;
    //k线图当前显示区域的最小值 的下标
    protected int mMainMinValueIndex = -1;
    //子图当前显示区域的最大值
    protected float mChildMaxValue = Float.MAX_VALUE;
    //子图当前实现区域的最小值
    protected float mChildMinValue = Float.MIN_VALUE;
    //显示区域中X开始点在数组的位置
    protected int mStartIndex = 0;
    //显示区域中X结束点在数组的位置
    protected int mStopIndex = 0;
    //一个点的宽度
    protected int mPointWidth = 6;
    //grid的行数
    protected int mGridRows = 4;
    //grid的列数
    private int mGridColumns = 2;
    //字体大小
    protected int mTextSize = 10;
    //表格画笔
    protected Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //文字画笔
    protected Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //选择线画笔
    protected Paint mLigthLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //时间画笔
    protected Paint mTimePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //背景画笔
    protected Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mCrossLabelBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mCrossLabelTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mSelectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mDashPaint;
    protected Paint mNewPricePaint;
    protected Rect mCrossLabelTextBound = new Rect();
    //长按之后选择的点的序号
    protected int mSelectedIndex;

    //数据适配器
    private IAdapter mAdapter;
    //数据观察者
    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            mItemCount = getAdapter().getCount();
            notifyChanged();
        }

        @Override
        public void onInvalidated() {
            mItemCount = getAdapter().getCount();
            notifyChanged();
        }
    };
    //当前点的个数
    private int mItemCount;
    //每个点的x坐标
    private List<Float> mXs = new ArrayList<>();
    private IChartDraw mMainDraw;//Main区域的画图方法
    private List<IChartDraw> mMainDraws = new ArrayList<>();
    private IChartDraw mChildDraw;//子区域的画图方法
    private List<IChartDraw> mChildDraws = new ArrayList<>();

    private IValueFormatter mValueFormatter;

    private CommonTabLayout mKChartTopTabView;
    private CommonTabLayout mKChartMiddleTabView;

    private ValueAnimator mAnimator;

    private long mAnimationDuration = 500;

    private float mOverScrollRange = 0;

    private OnSelectedChangedListener mOnSelectedChangedListener = null;

    private ArrayList<CustomTabEntity> topTabs = new ArrayList<>();

    private ArrayList<CustomTabEntity> middleTabs = new ArrayList<>();
    private boolean mIsFrist;
    private boolean mLand = false; //true 横屏  false 竖屏

    public BaseKChart(Context context) {
        super(context);
        init();
    }

    public BaseKChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseKChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void updateStyle(boolean mYeSyle){
        if (getContext() !=null){
            setColorStyle(getContext(),mYeSyle);
            postInvalidate();
        }
    }


    public void setIsFirstDraw(boolean land){
        this.mLand = land;
        if (mKChartTopTabView !=null){
            mKChartTopTabView.setVisibility(mLand ? GONE : VISIBLE);
            mKChartTopTabView.setIsFirstDraw();
        }
        if (mKChartMiddleTabView !=null){
            mKChartMiddleTabView.setVisibility(mLand ? GONE : VISIBLE);
            mKChartMiddleTabView.setIsFirstDraw();
        }
    }

    public void setTopTab(int position){
        if (mKChartTopTabView !=null){
            mKChartTopTabView.setCurrentTab(position);
            if (mKChartTopTabView.mListener !=null){
                mKChartTopTabView.mListener.onTabSelect(position,null);
            }
        }
    }

    public void setMiddleTab(int position){
        if (mKChartMiddleTabView !=null){
            mKChartMiddleTabView.setCurrentTab(position);
            if (mKChartMiddleTabView.mListener !=null){
                mKChartMiddleTabView.mListener.onTabSelect(position,null);
            }
        }
    }


    private int bgColor;
    private int gridLineColor;//虚线色值
    private int lineColor;
    private int textColor;
    private boolean mIsYeSyle =false;
    private void setColorStyle(Context context ,boolean mYeSyle){
        this.mIsYeSyle = mYeSyle;
        this.bgColor = mYeSyle ? ContextCompat.getColor(context, R.color.color_1F1C28) : ContextCompat.getColor(getContext(), R.color.color_ffffff);
        this.gridLineColor = mYeSyle ? ContextCompat.getColor(context, R.color.color_A4A5A6) : ContextCompat.getColor(context, R.color.color_f0f0f0);
        this.lineColor = mYeSyle ? ContextCompat.getColor(context, R.color.color_27252D) : ContextCompat.getColor(context, R.color.color_f0f0f0);
        this.textColor = mYeSyle ? ContextCompat.getColor(context, R.color.color_ffffff) : ContextCompat.getColor(context, R.color.color_323232);
        int mIndicatorColor =  mYeSyle ? ContextCompat.getColor(context, R.color.color_536072) : ContextCompat.getColor(context, R.color.color_f0f0f0);
        int mTextSelectColor =  mYeSyle ? ContextCompat.getColor(context, R.color.color_d8d7d7) : ContextCompat.getColor(context, R.color.color_878787);
        int mTextUnselectColor =  mYeSyle ? ContextCompat.getColor(context, R.color.color_b2b2b2) : ContextCompat.getColor(context, R.color.color_878787);
        if (mKChartTopTabView !=null){
            mKChartTopTabView.setBackgroundColor(bgColor);
            mKChartMiddleTabView.setBackgroundColor(bgColor);

            mKChartTopTabView.setUnderlineColor(lineColor);
            mKChartMiddleTabView.setUnderlineColor(lineColor);

            mKChartTopTabView.setIndicatorColor(mIndicatorColor);
            mKChartMiddleTabView.setIndicatorColor(mIndicatorColor);

            mKChartTopTabView.setTextSelectColor(mTextSelectColor);
            mKChartTopTabView.setTextUnselectColor(mTextUnselectColor);
            mKChartMiddleTabView.setTextSelectColor(mTextSelectColor);
            mKChartMiddleTabView.setTextUnselectColor(mTextUnselectColor);
        }
        if (mSelectorPaint !=null){
            mSelectorPaint.setColor(getResources().getColor(mYeSyle ?  R.color.color_2A334D : R.color.color_F8F8F8));
        }
    }

    private void init() {
        setWillNotDraw(false);
        mDetector = new GestureDetectorCompat(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);
        mTopPadding = dp2px(mTopPadding);
        mBottomPadding = dp2px(mBottomPadding);
        mMainTopSpace = dp2px(mMainChildSpace);
        mMainChildSpace = dp2px(mMainChildSpace);
        mMainTimeSpace = dp2px(mMainTimeSpace);
        mPointWidth = dp2px(mPointWidth);
        mTextSize = sp2px(mTextSize);
        mBackgroundPaint.setColor(getResources().getColor(R.color.color_ffffff));
        mCrossLabelBgPaint.setColor(getResources().getColor(R.color.color_5f6a83));
        mCrossLabelTextPaint.setColor(getResources().getColor(R.color.color_ffffff));
        mCrossLabelTextPaint.setTextSize(getResources().getDimension(R.dimen.dimen_10sp));

        mKChartMiddleTabView = (CommonTabLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.kline_tab_indicator, this, false);

        mKChartTopTabView = (CommonTabLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.kline_tab_indicator, this, false);

        mSelectorPaint.setColor(getResources().getColor(R.color.color_F8F8F8));
        //mSelectorPaint.setAlpha(200);

        setColorStyle(getContext(),AppApplication.getConfig().getYeStyle());

        mGridPaint.setColor(gridLineColor);
        mGridPaint.setStrokeWidth(dp2px(0.5f));
        mDashPaint = new Paint(mGridPaint);
        mDashPaint.setStyle(Paint.Style.STROKE);
        mDashPaint.setPathEffect(new DashPathEffect(new float[]{DeviceUtils.dip2px(getContext(), 2)
                , DeviceUtils.dip2px(getContext(), 2)}, 0));


        mNewPricePaint = new Paint();
        //mNewPricePaint.setStyle(Paint.Style.STROKE);
        mNewPricePaint.setStyle(Paint.Style.STROKE);
        mNewPricePaint.setAntiAlias(true);
        mNewPricePaint.setPathEffect(new DashPathEffect(new float[]{DeviceUtils.dip2px(getContext(), 2)
                , DeviceUtils.dip2px(getContext(), 2)}, 0));

        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setStrokeWidth(dp2px(0.5f));
        mRightInitPadding = -MinuteHourLineUtils.getTextWidth(mTextPaint, "00000.00");

        mLigthLinePaint.setColor(getResources().getColor(R.color.color_5f6a83));
        mLigthLinePaint.setStrokeWidth(dp2px(0.5f));

        mTimePaint.setColor(getResources().getColor(R.color.color_b2b2b2));
        mTimePaint.setTextSize(mTextSize);
        mTimePaint.setStrokeWidth(dp2px(0.5f));


        mKChartTopTabView.setOnTabSelectListener(new SimpleOnTabSelectListener() {
            @Override
            public void onTabSelect(int position, View tabLayout) {
                super.onTabSelect(position, tabLayout);
                setMainDraw(position);
                if (!mLand && onTabSelectListener !=null){
                    onTabSelectListener.onTopTabSelect(position);
                }
            }
        });
        addView(mKChartTopTabView, new LayoutParams(-1, dp2px(30)));


        mKChartMiddleTabView.setOnTabSelectListener(new SimpleOnTabSelectListener() {
            @Override
            public void onTabSelect(int position, View tabLayout) {
                super.onTabSelect(position, tabLayout);
                setChildDraw(position);
                if (!mLand && onTabSelectListener !=null){
                    onTabSelectListener.onMiddleTabSelect(position);
                }
            }
        });
        addView(mKChartMiddleTabView, new LayoutParams(-1, dp2px(30)));

        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.setDuration(mAnimationDuration);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMainTopSpace = mKChartTopTabView.getVisibility() == VISIBLE ? mKChartTopTabView.getMeasuredHeight() : 0;
        mMainChildSpace = mKChartMiddleTabView.getVisibility() == VISIBLE ? mKChartMiddleTabView.getMeasuredHeight() : 0;
        int displayHeight = h - mTopPadding - mBottomPadding - mMainTopSpace - mMainChildSpace - mMainTimeSpace;
        this.mMainHeight = (int) (displayHeight * 0.67f);
        this.mChildHeight = (int) (displayHeight * 0.33f);
        this.mWidth = w;
        mKChartMiddleTabView.setTranslationY(mMainHeight + mTopPadding + mMainTopSpace + mMainTimeSpace);
        setTranslateXFromScrollX(mScrollX);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(bgColor);
        mGridPaint.setColor(gridLineColor);
        mDashPaint.setColor(gridLineColor);
        mTextPaint.setColor(textColor);
        if (mWidth == 0 || mMainHeight == 0 || mItemCount == 0) {
            return;
        }

        if (mMainDraw == null) mMainDraw = mMainDraws.get(0);
        if (mChildDraw == null) mChildDraw = mChildDraws.get(0);

        calculateValue();
        canvas.save();
        canvas.translate(0, mTopPadding);
        canvas.scale(1, 1);
        drawGird(canvas);
        drawK(canvas);
        drawText(canvas);
        drawValue(canvas, isLongPress ? mSelectedIndex : mStopIndex);

        drawMaxMinRect(canvas, mMainMaxVaueIndex, mMainMinValueIndex);
        if (mIsNewPrice){
            drawNewPriceLine(canvas);
        }
        drawCross(canvas);

        canvas.restore();
    }

    public float getMainY(float value) {
        return (mMainMaxValue - value) * mMainScaleY + mMainTopSpace;
    }

    public float getChildY(float value) {
        return (mChildMaxValue - value) * mChildScaleY + mMainHeight + mMainTopSpace + mMainChildSpace + mMainTimeSpace;
    }

    /**
     * 解决text居中的问题
     */
    public float fixTextY(float y) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        return (y + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent);
    }

    /**
     * 画表格
     *
     * @param canvas
     */
    private void drawGird(Canvas canvas) {
        //-----------------------上方k线图------------------------
        //横向的grid
        float rowSpace = mMainHeight / 2;
        for (int i = 0; i < 3; i++) {
            if (i == 2) {
                Paint paint = new Paint(mGridPaint);
                paint.setColor(lineColor);
                canvas.drawLine(0, rowSpace * i + mMainTopSpace, mWidth, rowSpace * i + mMainTopSpace, paint);
            } else if (i == 1) {
                Path path = new Path();
                path.moveTo(0, rowSpace * i + mMainTopSpace);
                path.lineTo(mWidth, rowSpace * i + mMainTopSpace);
                canvas.drawPath(path, mDashPaint);
            }
        }
        mGridPaint.setColor(lineColor);
        canvas.drawLine(0, mMainHeight + mMainTopSpace + mMainTimeSpace + mTopPadding - mGridPaint.getStrokeWidth()
                , mWidth, mMainHeight + mMainTopSpace + mMainTimeSpace + mTopPadding, mGridPaint);// time separator line

        //纵向的grid
//        float columnSpace = mWidth / mGridColumns;
//        for (int i = 0; i <= mGridColumns; i++) {
//            canvas.drawLine(columnSpace * i, 0, columnSpace * i, mMainHeight, mGridPaint);
//            canvas.drawLine(columnSpace * i, mMainHeight + mMainChildSpace, columnSpace * i, mMainHeight + mMainChildSpace + mChildHeight, mGridPaint);
//        }

        //-----------------------下方子图------------------------
//        canvas.drawLine(0, mMainHeight + mMainChildSpace, mWidth, mMainHeight + mMainChildSpace, mGridPaint);
//        canvas.drawLine(0, mMainHeight + mMainChildSpace + mChildHeight, mWidth, mMainHeight + mMainChildSpace + mChildHeight, mGridPaint);
    }

    /**
     * 画k线图
     *
     * @param canvas
     */
    private void drawK(Canvas canvas) {
        //保存之前的平移，缩放
        canvas.save();
        canvas.translate(mTranslateX * mScaleX, 0);
        canvas.scale(mScaleX, 1);
        for (int i = mStartIndex; i <= mStopIndex; i++) {
            Object currentPoint = getItem(i);
            if (currentPoint == null){
                continue;
            }
            float currentPointX = getX(i);
            Object lastPoint = i == 0 ? currentPoint : getItem(i - 1);
            if (lastPoint == null){
                continue;
            }
            float lastX = i == 0 ? currentPointX : getX(i - 1);

            // 横向网格虚线
            if (i > 0 && (i % 20 == 0)) {
                // main dash separator line
                Path mainPath = new Path();
                mainPath.moveTo(currentPointX, mMainTopSpace);
                mainPath.lineTo(currentPointX, mMainHeight + mMainTopSpace);
                canvas.drawPath(mainPath, mDashPaint);

                // child dash separator line
                Path childPath = new Path();
                childPath.moveTo(currentPointX, mMainTopSpace + mMainHeight + mMainTimeSpace + mMainChildSpace);
                childPath.lineTo(currentPointX, mMainTopSpace + mMainHeight + mMainTimeSpace + mMainChildSpace + mChildHeight);
                canvas.drawPath(childPath, mDashPaint);
            }

            if (mMainDraw != null) {
                mMainDraw.drawTranslated(lastPoint, currentPoint, lastX, currentPointX, canvas, this, i);
            }
            if (mChildDraw != null) {
                mChildDraw.drawTranslated(lastPoint, currentPoint, lastX, currentPointX, canvas, this, i);
            }
        }

        canvas.restore();//还原 平移缩放
    }

    /**
     * 绘制十字线
     */
    private void drawCross(Canvas canvas) {
        if (isLongPress()) {
            mIsFrist = true;
            Paint.FontMetrics fontMetrics = mCrossLabelTextPaint.getFontMetrics();

            KLine point = (KLine) getItem(mSelectedIndex);
            if (point == null){
                return;
            }
            String text = formatValue(point.getCloseVal());
            String date = getAdapter().getDate(mSelectedIndex);
            float offset = getResources().getDimension(R.dimen.dimen_2dp);

            // 十字线
            float x = (getX(mSelectedIndex) + mTranslateX) * mScaleX;
            float y = getMainY(point.getCloseVal());
            canvas.drawLine(x, mMainTopSpace + mCrossLabelTextBound.height(), x, mMainHeight + mMainTopSpace, mLigthLinePaint);
            // canvas.drawLine(0, y, mWidth, y, mTextPaint);
            float rowsY = Math.max(mMainTopSpace
                    , Math.min(mMainTopSpace + mMainHeight, currentY));
            //canvas.drawLine(0, rowsY, mWidth, rowsY, mTextPaint);
            canvas.drawLine(0 + (translateXtoX(getX(mSelectedIndex)) < getChartWidth() / 2 ? mCrossLabelTextPaint.measureText(text) + offset * 2 : 0), rowsY, mWidth - (translateXtoX(getX(mSelectedIndex)) < getChartWidth() / 2 ? 0 : mCrossLabelTextPaint.measureText(text) + offset * 2), rowsY, mLigthLinePaint);

            canvas.drawLine(x, mMainHeight + mMainChildSpace + mMainTopSpace + mMainTimeSpace, x
                    , mMainHeight + mMainTopSpace + mMainChildSpace + mMainTimeSpace + mChildHeight, mLigthLinePaint);

            // X 轴标线值
            mCrossLabelTextPaint.getTextBounds(date, 0, date.length(), mCrossLabelTextBound);

            canvas.drawRect(x - mCrossLabelTextBound.width() / 2
                    , mMainTopSpace - Math.abs(fontMetrics.ascent)
                    , x + mCrossLabelTextBound.width() / 2 + offset * 2
                    , mMainTopSpace + mCrossLabelTextBound.height() + Math.abs(fontMetrics.descent)
                    , mCrossLabelBgPaint);
            canvas.drawText(date
                    , x - mCrossLabelTextBound.width() / 2 + getResources().getDimension(R.dimen.dimen_1_5dp)
                    , mMainTopSpace + Math.abs(fontMetrics.ascent) - Math.abs(fontMetrics.descent) / 3
                    , mCrossLabelTextPaint);

            // Y 轴标线值
            float textHeight = fontMetrics.descent - fontMetrics.ascent;
            float r = textHeight / 2;
            float borderTop = Math.max(mMainTopSpace + r, Math.min(mMainTopSpace + mMainHeight - r, rowsY));
            if (translateXtoX(getX(mSelectedIndex)) < getChartWidth() / 2) {
                x = 0;
                canvas.drawRect(x, borderTop - r, mCrossLabelTextPaint.measureText(text) + offset * 2, borderTop + r, mCrossLabelBgPaint);
            } else {
                x = mWidth - mCrossLabelTextPaint.measureText(text) - offset * 2;
                canvas.drawRect(x, borderTop - r, mWidth, borderTop + r, mCrossLabelBgPaint);
            }
            String rowsText = formatValue(getRowsValue(rowsY));
            canvas.drawText(rowsText, x + offset, fixTextY(borderTop), mCrossLabelTextPaint);

             drawSelector(this, canvas);

            if (KLineChartController.getInstance() != null &&
                    KLineChartController.getInstance().getOnActivityKlineOnTouchListener() != null) {

                KLineBean kLineBean = new KLineBean();
                kLineBean.open = point.getOpenVal();
                kLineBean.close = point.getCloseVal();
                kLineBean.high = point.getHighVal();
                kLineBean.low = point.getLowVal();
                kLineBean.date = date;
                KLineChartController.getInstance().getOnActivityKlineOnTouchListener().onValueSelected(kLineBean);
            }
        } else {
            if (mIsFrist)
                if (KLineChartController.getInstance() != null &&
                        KLineChartController.getInstance().getOnActivityKlineOnTouchListener() != null) {
                    KLineChartController.getInstance().getOnActivityKlineOnTouchListener().onNothingSelected();
                }
        }
    }



    private float newLinePrice ;
    private float  changeValue;
    private boolean mIsNewPrice;
    public void setNewLinePrice(float newLinePrice,float changeValue){
        this.newLinePrice = newLinePrice;
        this.changeValue = changeValue;
    }

    public float getNewLinePrice(){
        return newLinePrice;
    }


    public void setIsNewPrice(boolean mIsNewPrice){
        this.mIsNewPrice = mIsNewPrice;
    }

    private void drawNewPriceLine(Canvas canvas) {
        if (canvas == null) return;
        if (newLinePrice == 0 || newLinePrice > mMainMaxValue || newLinePrice < mMainMinValue){
            return;
        }
        float yUnit = getMainY(newLinePrice);

        int lineColor = Color.parseColor("#FF5376");//实线色值
        if (changeValue < 0) {
            lineColor = Color.parseColor("#00CE64");//实线色值
        }

        mNewPricePaint.setColor(lineColor);
        mNewPricePaint.setStyle(Paint.Style.STROKE);
        Path pathMain = new Path();
        pathMain.moveTo(0, yUnit);
        pathMain.lineTo(mWidth, yUnit);
        canvas.drawPath(pathMain, mNewPricePaint);

        mNewPricePaint.setStyle(Paint.Style.FILL);
        mNewPricePaint.setTextSize(getResources().getDimension(R.dimen.dimen_12sp));
        String newPriceStr = KLineUtils.getIntegerStr(newLinePrice);
        float textWidth = MinuteHourLineUtils.getTextWidth(mNewPricePaint, newPriceStr);
        float fontHeight = MinuteHourLineUtils.getFontHeight(getResources().getDimension(R.dimen.dimen_12sp));

        mNewPricePaint.setColor(lineColor);
        Path path = new Path();
        //path.moveTo(mWidth - textWidth - 5f - fontHeight, yUnit);
        path.moveTo(mWidth - textWidth, yUnit);

        //path.lineTo(mWidth - textWidth - fontHeight, yUnit  - fontHeight / 2);
        path.lineTo(mWidth - textWidth, yUnit  - fontHeight / 2);

        path.lineTo(mWidth, yUnit  - fontHeight / 2);
        path.lineTo(mWidth, yUnit  + fontHeight / 2);

        //path.lineTo(mWidth - textWidth - fontHeight, yUnit  + fontHeight / 2);
        path.lineTo(mWidth - textWidth , yUnit  + fontHeight / 2);

        //path.lineTo(mWidth - textWidth - 5f - fontHeight, yUnit);
        path.lineTo(mWidth - textWidth , yUnit);
        canvas.drawPath(path, mNewPricePaint);
        mNewPricePaint.setColor(Color.WHITE);
        //canvas.drawText(newPriceStr, mWidth - textWidth - fontHeight / 2, yUnit  + fontHeight / 4 + 1f, p);
        canvas.drawText(newPriceStr, mWidth - textWidth, yUnit  + fontHeight / 4 + 1f, mNewPricePaint);
    }



    /**
     * 通过当前长按坐标点获取Y轴对应数值
     */
    private float getRowsValue(float rowsY) {
        float y = rowsY - mMainTopSpace;
        return mMainMaxValue - (mMainMaxValue - mMainMinValue) * (y / mMainHeight);
    }

    /**
     * 绘制最大最小值
     */
    private void drawMaxMinRect(Canvas canvas, int maxIndex, int minIndex) {
        //绘制最大最小区域
        if (maxIndex != -1 && minIndex != -1) {
            // max
            KLineEntity maxPoint = (KLineEntity) getItem(maxIndex);
            if (maxPoint == null){
                return;
            }
            float currentPointXForMax = getX(maxIndex) * mScaleX + mTranslateX * mScaleX;
            boolean isShowLeft = false;
            if (currentPointXForMax > mWidth / 2) {
                isShowLeft = true;
            }
            String str = formatValue(maxPoint.getHighVal());
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#B2B2B2"));
            paint.setTextSize(24);
            Paint.FontMetrics fm = paint.getFontMetrics();
            float textHeight = fm.descent - fm.ascent;
            int strW = ViewUtil.getTextWidth(paint, str);
            int strH = (int) textHeight;
            float temY = getMainY(maxPoint.getHighVal()) - strH / 2;
            canvas.drawRect(currentPointXForMax + (isShowLeft ? -13 : 0), temY + (strH + 4) / 2 - 2, currentPointXForMax + (isShowLeft ? 0 : 13), temY + (strH + 4) / 2 + 2, paint);
            currentPointXForMax += (isShowLeft ? -13 : 13);
            canvas.drawRect(currentPointXForMax + (isShowLeft ? -(strW + 4) : 0), temY, currentPointXForMax + (isShowLeft ? 0 : strW + 4), temY + strH + 4, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText(str, currentPointXForMax + (isShowLeft ? -(strW + 2) : 2), temY + strH - 3, paint);

            // min
            KLineEntity minPoint = (KLineEntity) getItem(minIndex);
            if (minPoint == null){
                return;
            }
            float currentPointXForMin = getX(minIndex) * mScaleX + mTranslateX * mScaleX;
            isShowLeft = false;
            if (currentPointXForMin > mWidth / 2) {
                isShowLeft = true;
            }
            str = formatValue(minPoint.getLowVal());
            paint = new Paint();
            paint.setColor(Color.parseColor("#B2B2B2"));
            paint.setTextSize(24);
            fm = paint.getFontMetrics();
            strW = ViewUtil.getTextWidth(paint, str);
            strH = (int) (fm.descent - fm.ascent);
            temY = getMainY(minPoint.getLowVal()) - strH / 2;
            canvas.drawRect(currentPointXForMin + (isShowLeft ? -13 : 0), temY + (strH + 4) / 2 - 2, currentPointXForMin + (isShowLeft ? 0 : 13), temY + (strH + 4) / 2 + 2, paint);
            currentPointXForMin += (isShowLeft ? -13 : 13);
            canvas.drawRect(currentPointXForMin + (isShowLeft ? -(strW + 4) : 0), temY, currentPointXForMin + (isShowLeft ? 0 : strW + 4), temY + strH + 4, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText(str, currentPointXForMin + (isShowLeft ? -(strW + 2) : 2), temY + strH - 3, paint);
        }
    }

    /**
     * 画文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        int offset = DeviceUtils.dip2px(getContext(), 2.5f);

        //--------------画上方k线图的值-------------
        if (mMainDraw != null) {
            String mMainMaxValueStr = formatValue(mMainMaxValue);
            float textMainMaxValueWidth = MinuteHourLineUtils.getTextWidth(mTextPaint, mMainMaxValueStr);
            //canvas.drawText(formatValue(mMainMaxValue), offset, baseLine + mMainTopSpace, mTextPaint);
            canvas.drawText(mMainMaxValueStr, mWidth - textMainMaxValueWidth, baseLine + mMainTopSpace, mTextPaint);

            //canvas.drawText(formatValue(mMainMinValue), offset, mMainHeight + mMainTopSpace - fm.descent, mTextPaint);
            String mMainMinValueStr = formatValue(mMainMinValue);
            float textMainMinValueWidth = MinuteHourLineUtils.getTextWidth(mTextPaint, mMainMinValueStr);
            canvas.drawText(formatValue(mMainMinValue), mWidth - textMainMinValueWidth, mMainHeight + mMainTopSpace - fm.descent, mTextPaint);

            float rowValue = (mMainMaxValue - mMainMinValue) / mGridRows;
            float rowSpace = mMainHeight / mGridRows;
            for (int i = 1; i < mGridRows; i++) {
                String text = formatValue(rowValue * (mGridRows - i) + mMainMinValue);
                float textWidth = MinuteHourLineUtils.getTextWidth(mTextPaint, text);

                //canvas.drawText(text, DeviceUtils.dip2px(getContext(), 2.5f), fixTextY(rowSpace * i) + mMainTopSpace, mTextPaint);
                canvas.drawText(text, mWidth - textWidth, fixTextY(rowSpace * i) + mMainTopSpace, mTextPaint);
            }
        }

        //--------------画下方子图的值-------------
        if (mChildDraw != null) {
            int childTop = mMainHeight + mMainTopSpace + mMainChildSpace + mMainTimeSpace;
            int childBottom = mMainChildSpace + mMainHeight + mMainTopSpace + mChildHeight + mMainTimeSpace;
            int childMiddle = childTop + (childBottom - childTop) / 2;

            String mChildMaxValueText = formatValue(mChildMaxValue);
            float mChildMaxValueWidth = MinuteHourLineUtils.getTextWidth(mTextPaint, mChildMaxValueText);
            canvas.drawText(mChildMaxValueText, mWidth - mChildMaxValueWidth, childTop + Math.abs(fm.ascent) + offset, mTextPaint);

            String mChildMaxMinValueText = formatValue((mChildMaxValue + mChildMinValue) / 2);
            float mChildMaxMinValueWidth = MinuteHourLineUtils.getTextWidth(mTextPaint, mChildMaxMinValueText);
            canvas.drawText(mChildMaxMinValueText, mWidth - mChildMaxMinValueWidth, childMiddle, mTextPaint);


            String mChildMinValueText = formatValue(mChildMinValue);
            float mChildMinValueWidth = MinuteHourLineUtils.getTextWidth(mTextPaint, mChildMinValueText);
            canvas.drawText(mChildMinValueText, mWidth - mChildMinValueWidth, childBottom - Math.abs(fm.descent) - offset, mTextPaint);
        }

        //--------------画时间---------------------
        float columnSpace = mWidth / mGridColumns;
        float y = mMainHeight + mMainTopSpace;

        float startX = getX(mStartIndex) - mPointWidth / 2;
        float stopX = getX(mStopIndex) + mPointWidth / 2;

        // middle time
        for (int i = 1; i < mGridColumns; i++) {
            float translateX = xToTranslateX(columnSpace * i);
            if (translateX >= startX && translateX <= stopX) {
                int index = indexOfTranslateX(translateX);
                String text = mAdapter.getDate(index);
                canvas.drawText(text
                        , columnSpace * i - mTimePaint.measureText(text) / 2
                        , y + Math.abs(fm.ascent) + offset, mTimePaint);
            }
        }
        // first time
        float translateX = xToTranslateX(0);
        if (translateX >= startX && translateX <= stopX) {
            canvas.drawText(getAdapter().getDate(mStartIndex), offset, y + Math.abs(fm.ascent) + offset, mTimePaint);
        }
        // last time
        translateX = xToTranslateX(mWidth);
        if (translateX >= startX && translateX <= stopX) {
            String text = getAdapter().getDate(mStopIndex);
            canvas.drawText(text, mWidth - mTimePaint.measureText(text) - offset, y + Math.abs(fm.ascent) + offset, mTimePaint);
        }
    }

    /**
     * 画值
     *
     * @param canvas
     * @param position 显示某个点的值
     */
    private void drawValue(Canvas canvas, int position) {
        if (position >= 0 && position < mItemCount) {
            if (mMainDraw != null) {
                float x = 0;
                float y = dp2px(2) + mMainTopSpace;
                mMainDraw.drawText(canvas, this, position, x, y);
            }
            if (mChildDraw != null) {
                float x = 0;
                float y = dp2px(2) + mMainChildSpace + mMainHeight + mMainTopSpace + mMainTimeSpace;
                mChildDraw.drawText(canvas, this, position, x, y);
            }
        }
    }

    /**
     * 绘制当前点位提示信息
     *
     * @param view
     * @param canvas
     */
    protected void drawSelector(IKChartView view, Canvas canvas) {
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float textHeight = metrics.descent - metrics.ascent;

        int index = view.getSelectedIndex();
        float padding = DeviceUtils.dip2px(getContext(), 5);
        float margin = DeviceUtils.dip2px(getContext(), 5);
        float width = 0;
        float left;
        float top = margin;
        float height = padding * 7 + textHeight * 8;

        Candle point = (Candle) view.getItem(index);
        List<String> strings = new ArrayList<>();
        strings.add(view.getAdapter().getDate(index));
        strings.add("开盘:" + point.getOpenVal());
        strings.add("收盘:" + point.getCloseVal());
        strings.add("最高:" + point.getHighVal());
        strings.add("最低:" + point.getLowVal());
        strings.add("涨跌:" + point.getPriceNum());
        strings.add("涨幅:" + point.getPercentage());

        for (String s : strings) {
            width = Math.max(width, mTextPaint.measureText(s));
        }
        width += padding * 2;

        float x = view.translateXtoX(view.getX(index));
        if (x > view.getChartWidth() / 2) {
            left = margin;
        } else {
            left = view.getChartWidth() - width - margin;
        }

        RectF r = new RectF(left, top + mMainTopSpace, left + width, top + height + mMainTopSpace);
        canvas.drawRoundRect(r, padding, padding, mSelectorPaint);
        float y = top + padding * 2 + (textHeight - metrics.bottom - metrics.top) / 2 + mMainTopSpace;

//        for (String s : strings) {
//            canvas.drawText(s, left + padding, y, mTextPaint);
//            y += textHeight + padding;
//        }

        float textWidth = MinuteHourLineUtils.getTextWidth(mTextPaint, "最高: ");
        int color_878787 = getResources().getColor(R.color.color_878787);
        int color_F74F54 = getResources().getColor(R.color.color_F74F54);
        int color_1AC47A = getResources().getColor(R.color.color_1AC47A);
        int color_333333 = getResources().getColor(R.color.color_333333);
        int color_ffffff = getResources().getColor(R.color.color_ffffff);

        float mPriceNum = Float.parseFloat(point.getPriceNum());
        for (int i = 0 ; i< strings.size() ; i ++){
              if (i == 0){
                  mTextPaint.setColor(color_878787);
                  canvas.drawText(strings.get(i), left + padding, y, mTextPaint);
              }else if (i == 1){
                  mTextPaint.setColor(color_878787);
                  canvas.drawText("开盘:", left + padding, y, mTextPaint);
                  mTextPaint.setColor(color_F74F54);
                  canvas.drawText(String.valueOf(point.getOpenVal()), left + padding+textWidth, y, mTextPaint);
              }else if (i == 2){
                  mTextPaint.setColor(color_878787);
                  canvas.drawText("收盘:", left + padding, y, mTextPaint);
                  mTextPaint.setColor(point.getCloseVal() > point.getOpenVal() ? color_F74F54 : (point.getCloseVal() < point.getOpenVal() ? color_1AC47A : (mIsYeSyle ? color_ffffff : color_333333)));
                  canvas.drawText(String.valueOf(point.getCloseVal()), left + padding+textWidth, y, mTextPaint);
              }else if (i == 3){
                  mTextPaint.setColor(color_878787);
                  canvas.drawText("最高:", left + padding, y, mTextPaint);
                  mTextPaint.setColor(color_F74F54);
                  canvas.drawText(String.valueOf(point.getHighVal()), left + padding+textWidth, y, mTextPaint);
              }else if (i == 4){
                  mTextPaint.setColor(color_878787);
                  canvas.drawText("最低:", left + padding, y, mTextPaint);
                  mTextPaint.setColor(color_1AC47A);
                  canvas.drawText(String.valueOf(point.getLowVal()), left + padding+textWidth, y, mTextPaint);
              }else if (i == 5){
                  mTextPaint.setColor(color_878787);
                  canvas.drawText("涨跌:", left + padding, y, mTextPaint);
                  mTextPaint.setColor(mPriceNum > 0 ? color_F74F54 : (mPriceNum < 0 ? color_1AC47A : (mIsYeSyle ? color_ffffff : color_333333)));
                  canvas.drawText(point.getPriceNum(), left + padding+textWidth, y, mTextPaint);
              }else if (i == 6){
                  mTextPaint.setColor(color_878787);
                  canvas.drawText("涨幅:", left + padding, y, mTextPaint);
                  mTextPaint.setColor(mPriceNum > 0 ? color_F74F54 : (mPriceNum < 0 ? color_1AC47A : (mIsYeSyle ? color_ffffff : color_333333)));
                  canvas.drawText(point.getPercentage(), left + padding+textWidth, y, mTextPaint);
              }
              y += textHeight + padding;
        }
    }

    public int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    public String formatValue(float value) {
        if (getValueFormatter() == null) {
            setValueFormatter(new ValueFormatter());
        }
        return getValueFormatter().format(value);
    }

    /**
     * 重新计算并刷新线条
     */
    public void notifyChanged() {
        if (mItemCount != 0 && mXs !=null) {
            mXs.clear();
            mDataLen = (mItemCount - 1) * mPointWidth;
            for (int i = 0; i < mItemCount; i++) {
                float x = i * mPointWidth;
                mXs.add(x);
            }
            checkAndFixScrollX();
            setTranslateXFromScrollX(mScrollX);
        } else {
            setScrollX(0);
        }
        invalidate();
    }

    private void calculateSelectedX(float x) {
        mSelectedIndex = indexOfTranslateX(xToTranslateX(x));
        if (mSelectedIndex < mStartIndex) {
            mSelectedIndex = mStartIndex;
        }
        if (mSelectedIndex > mStopIndex) {
            mSelectedIndex = mStopIndex;
        }
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
        int lastIndex = mSelectedIndex;
        calculateSelectedX(e.getX());
        if (lastIndex != mSelectedIndex) {
            onSelectedChanged(this, getItem(mSelectedIndex), mSelectedIndex);
        }
        invalidate();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        setTranslateXFromScrollX(mScrollX);
    }

    /**
     * 计算当前的显示区域
     */
    private void calculateValue() {
        if (!isLongPress()) {
            mSelectedIndex = -1;
        }
        mMainMaxValue = Float.MIN_VALUE;
        mMainMinValue = Float.MAX_VALUE;
        mChildMaxValue = Float.MIN_VALUE;
        mChildMinValue = Float.MAX_VALUE;
        mStartIndex = indexOfTranslateX(xToTranslateX(0));
        mStopIndex = indexOfTranslateX(xToTranslateX(mWidth));
        float tempMax = Float.MIN_VALUE;
        float tempMin = Float.MAX_VALUE;
        for (int i = mStartIndex; i <= mStopIndex; i++) {
            KLine point = (KLine) getItem(i);
            if (point == null){
                continue;
            }
            if (mMainDraw != null) {
                mMainMaxValue = Math.max(mMainMaxValue, mMainDraw.getMaxValue(point));
                mMainMinValue = Math.min(mMainMinValue, mMainDraw.getMinValue(point));

                if (point.getHighVal() > tempMax) {
                    tempMax = point.getHighVal();
                    mMainMaxVaueIndex = i;
                }

                if (point.getLowVal() < tempMin) {
                    tempMin = point.getLowVal();
                    mMainMinValueIndex = i;
                }
            }
            if (mChildDraw != null) {
                mChildMaxValue = Math.max(mChildMaxValue, mChildDraw.getMaxValue(point));
                mChildMinValue = Math.min(mChildMinValue, mChildDraw.getMinValue(point));
            }
        }
        float padding = (mMainMaxValue - mMainMinValue) * 0.12f;
        mMainMaxValue += padding;
        mMainMinValue -= padding;
        mMainScaleY = mMainHeight * 1f / (mMainMaxValue - mMainMinValue);
        mChildScaleY = mChildHeight * 1f / (mChildMaxValue - mChildMinValue);
        if (mAnimator.isRunning()) {
            float value = (float) mAnimator.getAnimatedValue();
            mStopIndex = mStartIndex + Math.round(value * (mStopIndex - mStartIndex));
        }
    }

    /**
     * 获取平移的最小值
     *
     * @return
     */
    private float getMinTranslateX() {
        return -mDataLen + mWidth / mScaleX - mPointWidth / 2;
    }

    /**
     * 获取平移的最大值
     *
     * @return
     */
    private float getMaxTranslateX() {
        if (!isFullScreen()) {
            return getMinTranslateX();
        }
        return mPointWidth / 2;
    }

    @Override
    public int getMinScrollX() {
        return (int) -(mOverScrollRange / mScaleX);
    }

    public int getMaxScrollX() {
        return Math.round(getMaxTranslateX() - getMinTranslateX());
    }

    public int indexOfTranslateX(float translateX) {
        return indexOfTranslateX(translateX, 0, mItemCount - 1);
    }

    @Override
    public void drawMainLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        canvas.drawLine(startX, getMainY(startValue), stopX, getMainY(stopValue), paint);
    }

    @Override
    public void drawChildLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        canvas.drawLine(startX, getChildY(startValue), stopX, getChildY(stopValue), paint);
    }

    @Override
    public Object getItem(int position) {
        if (mAdapter != null) {
            try {
                return mAdapter.getItem(position);
            }catch (Exception e){
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public float getX(int position) {
        if (mXs !=null && mXs.size() > position){
            return mXs.get(position);
        }else {
            return 0;
        }
    }

    @Override
    public IAdapter getAdapter() {
        return mAdapter;
    }

    public int getMainHeight() {
        return mMainHeight;
    }

    public int getTabBarHeight() {
        return mMainChildSpace;
    }

    /**
     * 设置子图的绘制方法
     */
    protected void setChildDraw(int position) {
        this.mChildDraw = mChildDraws.get(position);
        mChildDrawPosition = position;
        invalidate();
    }

    protected void setMainDraw(int position) {
        this.mMainDraw = mMainDraws.get(position);
        mMainDrawPosition = position;
        invalidate();
    }

    @Override
    public void addChildDraw(String name, IChartDraw childDraw) {
        mChildDraws.add(childDraw);
        middleTabs.add(new TabEntity(name));
    }

    @Override
    public void addMainDraw(String name, IChartDraw mainDraw) {
        mMainDraws.add(mainDraw);
        topTabs.add(new TabEntity(name));
    }

    /**
     * scrollX 转换为 TranslateX
     *
     * @param scrollX
     */
    private void setTranslateXFromScrollX(int scrollX) {
        mTranslateX = scrollX + getMinTranslateX() + mRightInitPadding;
    }

    /**
     * 获取ValueFormatter
     *
     * @return
     */
    public IValueFormatter getValueFormatter() {
        return mValueFormatter;
    }

    /**
     * 设置ValueFormatter
     *
     * @param valueFormatter value格式化器
     */
    public void setValueFormatter(IValueFormatter valueFormatter) {
        this.mValueFormatter = valueFormatter;
    }

    /**
     * 二分查找当前值的index
     *
     * @param translateX
     * @return
     */
    public int indexOfTranslateX(float translateX, int start, int end) {
        try {
            if (end == start) {
                return start;
            }
            if (end - start == 1) {
                float startValue = getX(start);
                float endValue = getX(end);
                return Math.abs(translateX - startValue) < Math.abs(translateX - endValue) ? start : end;
            }
            int mid = start + (end - start) / 2;
            float midValue = getX(mid);
            if (translateX < midValue) {
                return indexOfTranslateX(translateX, start, mid);
            } else if (translateX > midValue) {
                return indexOfTranslateX(translateX, mid, end);
            } else {
                return mid;
            }
        }catch (StackOverflowError e){
            e.printStackTrace();
            return start;
        }
    }

    @Override
    public void setAdapter(IAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataSetObserver);
            mItemCount = mAdapter.getCount();
        } else {
            mItemCount = 0;
        }
        notifyChanged();
    }

    @Override
    public void startAnimation() {
        if (mAnimator != null) {
            mAnimator.start();
        }
    }

    @Override
    public void setAnimationDuration(long duration) {
        if (mAnimator != null) {
            mAnimator.setDuration(duration);
        }
    }

    /**
     * 设置表格行数
     *
     * @param gridRows
     */
    public void setGridRows(int gridRows) {
        if (mGridRows < 1) {
            mGridRows = 1;
        }
        mGridRows = gridRows;
        invalidate();
    }

    /**
     * 设置表格列数
     *
     * @param gridColumns
     */
    public void setGridColumns(int gridColumns) {
        if (gridColumns < 1) {
            gridColumns = 1;
        }
        mGridColumns = gridColumns;
        invalidate();
    }

    @Override
    public float xToTranslateX(float x) {
        return -mTranslateX + x / mScaleX;
    }

    @Override
    public float translateXtoX(float translateX) {
        return (translateX + mTranslateX) * mScaleX;
    }

    @Override
    public float getTopPadding() {
        return mTopPadding;
    }

    @Override
    public int getChartWidth() {
        return mWidth;
    }

    @Override
    public boolean isLongPress() {
        return isLongPress;
    }

    @Override
    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    @Override
    public void setOnSelectedChangedListener(OnSelectedChangedListener l) {
        this.mOnSelectedChangedListener = l;
    }

    @Override
    public void onSelectedChanged(IKChartView view, Object point, int index) {
        if (this.mOnSelectedChangedListener != null) {
            mOnSelectedChangedListener.onSelectedChanged(view, point, index);
        }
    }

    /**
     * 数据是否充满屏幕
     *
     * @return
     */
    public boolean isFullScreen() {
        return mDataLen >= mWidth / mScaleX;
    }

    @Override
    public void setOverScrollRange(float overScrollRange) {
        if (overScrollRange < 0) {
            overScrollRange = 0;
        }
        mOverScrollRange = overScrollRange;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mKChartTopTabView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mKChartTopTabView.setTabData(topTabs);
                mKChartMiddleTabView.setTabData(middleTabs);
            }
        }, 400);
    }

    private OnTabSelectListener onTabSelectListener;
    public void setOnTabSelectListener(OnTabSelectListener listener){
        this.onTabSelectListener = listener;
    }
    public interface OnTabSelectListener{
        void onTopTabSelect(int position);
        void onMiddleTabSelect(int position);
    }
}
