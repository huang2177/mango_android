package com.paizhong.manggo.ui.kchart.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.paizhong.manggo.utils.DeviceUtils;

/**
 * 分时图行情气泡View
 * Created by zq on 2017/8/2.
 */

public class PopupView extends View {
    private Context mContext;

    private float mPopX;
    private float mPopY;
    private float mTriangleLength;
    private float mPopupViewRadius;
    private float mRectWidth;
    private float mRectHeight;
    private int mYellowStarNum;//黄色星星数量
    private int mWhiteStarNum;//白色星星数量
    private static final int POP_BG_COLOR = Color.parseColor("#99000000");
    private static final int POP_COLOR_WHITE = Color.parseColor("#FFFFFF");
    private static final int COLOR_STAR_YELLOW = Color.parseColor("#FFCA02");

    public PopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initData();
    }

    private void initData() {
        mTriangleLength = DeviceUtils.dip2px(mContext, 5.f);
        mPopupViewRadius = DeviceUtils.dip2px(mContext, 10.f);
        mRectWidth = DeviceUtils.dip2px(mContext, 172.f);
        mRectHeight = DeviceUtils.dip2px(mContext, 61.f);

        // TODO: 2017/8/2 zq  暂写死星星数量，正式需要接口获取
        mYellowStarNum = 4;
        mWhiteStarNum = 1;
    }

    public void updatePopupViewLocation(float popX, float popY) {
        this.mPopX = popX;
        this.mPopY = popY;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPopView(canvas);
    }

    private void drawPopView(Canvas canvas) {
        if (canvas == null) return;
        int textLeftMargin = DeviceUtils.dip2px(mContext, 10.f);
        int textTopMargin = DeviceUtils.dip2px(mContext, 19.f);
        int starWidth = DeviceUtils.dip2px(mContext, 10.f);
        int starLeftMargin = DeviceUtils.dip2px(mContext, 15.f);
        float text12Size = DeviceUtils.sp2px(mContext, 12);
        float text10Size = DeviceUtils.sp2px(mContext, 10);
        float textLine2Width;//第二行文本占用宽度
        float textLine3Width;//第三行文本占用宽度

        Paint paint = new Paint();
        paint.setColor(POP_BG_COLOR);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        //绘制气泡底部小三角
        Path path = new Path();
        path.moveTo(mPopX, mPopY);
        path.lineTo(mPopX - mTriangleLength, mPopY - mTriangleLength);
        path.lineTo(mPopX + mTriangleLength, mPopY - mTriangleLength);
        path.close();
        canvas.drawPath(path, paint);

        //绘制气泡圆角矩形
        RectF rectF = new RectF();
        rectF.left = mPopX - mRectWidth / 2;
        rectF.right = mPopX + mRectWidth / 2;
        rectF.top = mPopY - mTriangleLength - mRectHeight;
        rectF.bottom = mPopY - mTriangleLength;
        canvas.drawRoundRect(rectF, mPopupViewRadius, mPopupViewRadius, paint);

        //绘制第一行和第二行文本
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(POP_COLOR_WHITE);
        paint.setTextSize(text12Size);
        canvas.drawText("美国当周API汽油库存", rectF.left + textLeftMargin, rectF.top + textTopMargin, paint);
        paint.setTextSize(text10Size);
        textLine2Width = paint.measureText("前值 485   预期 498   预期 待公布");
        canvas.drawText("前值 485   预期 498   预期 待公布", rectF.left + textLeftMargin, rectF.top + textTopMargin * 1.8f, paint);


        //绘制五角星
        float starX = rectF.left + starLeftMargin;
        float starY = rectF.top + textTopMargin * 2.2f;
        drawStars(canvas, starX, starY, starWidth, starLeftMargin, paint);

        //绘制第三行五角星后的文本
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(POP_COLOR_WHITE);
        paint.setTextSize(text12Size);
        textLine3Width = paint.measureText("20:30");
        canvas.drawText("20:30", rectF.left + textLeftMargin + textLine2Width - textLine3Width, starY + text10Size, paint);
        paint.reset();

    }

    private void drawStars(Canvas canvas, float starX, float starY, float starWidth, float starLeftMargin, Paint paint) {
        float starX2;
        for (int i = 0; i < mYellowStarNum + mWhiteStarNum; i++) {
            starX2 = starX + starLeftMargin * i;
            if (i < mYellowStarNum) {
                paint.setColor(COLOR_STAR_YELLOW);
            } else {
                paint.setColor(POP_COLOR_WHITE);
            }

            Path starPath = new Path();
            starPath.moveTo(starX2, starY);
            starPath.lineTo(starX2 + starWidth / 3, starY + starWidth);
            starPath.lineTo(starX2 - starWidth / 2, starY + starWidth / 3);
            starPath.lineTo(starX2 + starWidth / 2, starY + starWidth / 3);
            starPath.lineTo(starX2 - starWidth / 3, starY + starWidth);
            starPath.close();
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(starPath, paint);
        }
    }
}
