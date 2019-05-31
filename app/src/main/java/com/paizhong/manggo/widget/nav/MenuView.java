package com.paizhong.manggo.widget.nav;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;

/**
 * Des: 底部导航栏 ItemView
 * Created by huang on 2018/8/28 0028 17:19
 */
public class MenuView extends LinearLayout {
    private ImageView ivTop;
    private ImageView ivBottom;
    private TextView tvTitle;

    /**
     * 标记是否选中
     */
    private boolean checked;
    private Drawable drawable;

    private int radius;
    private boolean isNeedRedDot;

    private Paint paint;


    public MenuView(Context context, int imageRes, String text, int position) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.nav_menu_layout, this, true);
        initView(position, imageRes, text);
    }

    public MenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView(int position, int mImageRes, String text) {
        paint = new Paint();
        paint.setAntiAlias(true);
        radius = getResources().getDimensionPixelSize(R.dimen.dimen_4dp);

        tvTitle = findViewById(R.id.nav_tv);
        ivTop = findViewById(R.id.nav_iv_top);
        ivBottom = findViewById(R.id.nav_iv_bottom);

        Drawable up = ContextCompat.getDrawable(getContext(), mImageRes);
        if (up != null) {
            drawable = DrawableCompat.wrap(up);
        }
        tvTitle.setText(text);

        setTag(position);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_ripple));
        }
    }


    /**
     * 设置是否选中
     *
     * @param checked
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
        tvTitle.setSelected(checked);
        ivBottom.setSelected(checked);
        setTint(checked);

        if (checked) {
            setNeedRedDot(false);//选中时红点消失
        }
    }

    /**
     * 通过着色器设置颜色
     *
     * @param checked
     */
    private void setTint(boolean checked) {
        int tintColor = checked ? R.color.color_333333 : R.color.color_B1B3B2;
        DrawableCompat.setTint(drawable, ContextCompat.getColor(getContext(), tintColor));
        ivTop.setImageDrawable(drawable);
    }

    public boolean isChecked() {
        return checked;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (checked) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setTint(true);
                break;
            case MotionEvent.ACTION_UP:
                setTint(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                setTint(false);
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (isNeedRedDot) {
            int left = (getWidth() + getDrawableWidth()) / 2 + radius; //（加上radius是为了距离图片有一定的间距）
            int top = getResources().getDimensionPixelSize(R.dimen.dimen_10dp);
            paint.setColor(Color.RED);
            canvas.drawCircle(left, top, radius, paint);
        }
        return super.drawChild(canvas, child, drawingTime);
    }

    /**
     * 设置是否需要显示红点
     *
     * @param needRedDot
     */
    public void setNeedRedDot(boolean needRedDot) {
        if (isNeedRedDot == needRedDot) {
            return;
        }
        isNeedRedDot = needRedDot;
        invalidate();
    }

    private int getDrawableWidth() {
        return drawable.getIntrinsicWidth();
    }
}
