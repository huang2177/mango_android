package com.paizhong.manggo.ui.kchart.views;

/**
 * 十字线移动的监听
 */
public interface OnMoveListener {
    /**
     * 十字线移动(回调到数据存放的位置，判断是否需要画线后，再调用本界面画线方法)
     *
     * @param x x轴坐标
     * @param y y轴坐标
     */
    void onCrossMove(float x, float y);

}
