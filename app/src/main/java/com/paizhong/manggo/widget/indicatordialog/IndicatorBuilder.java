package com.paizhong.manggo.widget.indicatordialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 作者:huangshuang
 * 事件 2017/5/18.
 * 邮箱： 15378412400@163.com
 */

public class IndicatorBuilder {
    public static final int TOP = 12;
    public static final int BOTTOM = 13;
    public static final int LEFT = 14;
    public static final int RIGHT = 15;

    public static final int GRAVITY_LEFT = 688;
    public static final int GRAVITY_RIGHT = 689;
    public static final int GRAVITY_CENTER = 670;

    protected int width;
    protected int height;
    protected int radius = 8;
    protected int bgColor = Color.TRANSPARENT;
    protected int mArrowWidth;
    /**
     * 箭头的位置
     */
    protected float arrowPercentage;
    protected int arrowdirection = TOP;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected BaseRecyclerAdapter mAdapter;
    protected int gravity = GRAVITY_LEFT;
    protected int animator;
    protected BaseDrawable mArrowDrawable;
    protected float alpha = 0.5f;
    protected float cardElevation = 0f;
    protected boolean enableTouchOutside = true;
    protected boolean enableDismissView;
    private Activity mContext;
    protected View.OnClickListener listener;
    protected BaseRecyclerAdapter.OnItemClick itemClickListener;
    public DialogInterface.OnDismissListener dismissListener;

    /**
     * 提供默认的数据类型，若是需要其他的数据类型，则需要实现BaseIndicatorAdapter自己实现
     */

    public IndicatorBuilder(Activity context) {
        this.mContext = context;
    }

    /**
     * 对话框宽度
     *
     * @param width px
     * @return
     */
    public IndicatorBuilder width(int width) {
        this.width = width;
        return this;
    }

    /**
     * 对话框高度， -1则自动适配，否则如果内容真实的高度大于指定的高度，则使用指定的高度，否则使用真实的高度
     *
     * @param height 高度，单位px
     * @return
     */
    public IndicatorBuilder height(int height) {
        this.height = height;
        return this;
    }

    /**
     * 对话框背景颜色
     *
     * @param color
     * @return
     */
    public IndicatorBuilder bgColor(int color) {
        this.bgColor = color;
        return this;

    }

    /**
     * dialog的圆角度数 必须 >= 0
     *
     * @param radius 四周圆角度数
     * @return
     */
    public IndicatorBuilder radius(int radius) {
        if (radius < 0) {
            new Exception("radius must >=0");
        }
        this.radius = radius;
        return this;
    }

    /**
     * 为dialog添加进入退出动画
     *
     * @param animator
     * @return
     */
    public IndicatorBuilder animator(@StyleRes int animator) {
        this.animator = animator;
        return this;
    }

    /**
     * 三角箭头的宽高，因为他是正方形的。
     * 如果不填则默认是用 {@IndicatorDialog.ARROW_RECTAGE} 这个属性，把Dialog的width属性取出来 mBuilder.width * ARROW_RECTAGE 来获取这个箭头的宽度
     * 单位px
     * 当然，你也可以修改{@IndicatorDialog.ARROW_RECTAGE} 这个属性，但是注意： 它是static的，所以会对全局起效。
     *
     * @param width 箭头的宽高
     * @return
     */
    public IndicatorBuilder arrowWidth(int width) {
        this.mArrowWidth = width;
        return this;
    }

    /**
     * 箭头的drawable，你可以通过继承BaseDrawable来实现自定义箭头的样式，默认会将箭头的高度/2往view方向偏移。
     * 不填则是用默认的三角箭头
     *
     * @param drawable
     * @return
     */
    public IndicatorBuilder arrowDrawable(BaseDrawable drawable) {
        this.mArrowDrawable = drawable;
        return this;
    }

    /**
     * 箭头的位置，如果是上下方向，则 view的位置为 width*rectage,如果是左右方向，则 view的位置为 {@IndicatorDialog.mResultHeight} *rectage
     *
     * @param rectage
     * @return
     */
    public IndicatorBuilder arrowRectage(float rectage) {
        if (rectage > 1 || rectage < 0) {
            new Exception("rectage must be 0 <= rectage <= 1");
        }
        this.arrowPercentage = rectage;
        return this;
    }

    /**
     * 箭头方向
     *
     * @param direction
     * @return
     */
    public IndicatorBuilder arrowDirection(@ARROWDIRECTION int direction) {
        this.arrowdirection = direction;
        return this;
    }


    /**
     * //设置背景透明度，0~1.0  默认0.5
     *
     * @param alpha 默认0.5
     * @return
     */
    public IndicatorBuilder dimAmount(float alpha) {
        this.alpha = alpha;
        return this;
    }


    public IndicatorBuilder layoutManager(RecyclerView.LayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        return this;
    }

    public IndicatorBuilder adapter(BaseRecyclerAdapter adapter) {
        this.mAdapter = adapter;
        return this;
    }

    public IndicatorBuilder gravity(@GRAVITY int gravity) {
        this.gravity = gravity;
        return this;
    }

    public IndicatorBuilder enableTouchOutside(boolean enableTouchOutside) {
        this.enableTouchOutside = enableTouchOutside;
        return this;
    }

    public IndicatorBuilder enableDismissView(boolean enableDismissView) {
        this.enableDismissView = enableDismissView;
        return this;
    }


    public IndicatorBuilder listener(View.OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    public IndicatorBuilder itemListener(BaseRecyclerAdapter.OnItemClick onItemClick) {
        this.itemClickListener = onItemClick;
        return this;
    }

    public IndicatorBuilder dismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.dismissListener = onDismissListener;
        return this;
    }

    public IndicatorDialog create() {
        if (width <= 0) {
            throw new NullPointerException("width can not be 0");
        }

        if (arrowPercentage < 0) {
            throw new NullPointerException("arrowPercentage can not < 0");
        }

        if (mAdapter != null) {
            mAdapter.setOnItemClickListener(itemClickListener);
        }

        if (mLayoutManager == null) {
            mLayoutManager = new LinearLayoutManager(mContext) {
                @Override
                public boolean canScrollVertically() {
                    return false; //禁止上下滑动
                }
            };
        }
        return IndicatorDialog.newInstance(mContext, this);
    }

    @IntDef({TOP, BOTTOM, LEFT, RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ARROWDIRECTION {
    }

    @IntDef({GRAVITY_LEFT, GRAVITY_RIGHT, GRAVITY_CENTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GRAVITY {

    }
}
