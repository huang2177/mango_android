package com.paizhong.manggo.ui.home.module;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.bar.SystemBarConfig;

/**
 * Des:（新手引导，挂单布局）控制类
 * Created by huang on 2018/10/30 0030 14:36
 */
public class BottomControlHelper {
    private static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    private static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    /***
     * 初始化底部控制布局
     * @param activity
     * @param isAuditing
     */
    public static void init(Activity activity, boolean isAuditing) {
        if (isAuditing) {
            return;
        }

        ViewGroup container = null;
        if (activity == null) {
            return;
        }
        if (activity instanceof MainActivity) {
            container = ((MainActivity) activity).mContainer;
        }
        if (container == null) {
            container = activity.findViewById(R.id.bottom_container);
        }
        if (container == null) {
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            LinearLayout bottomContainer = new LinearLayout(activity);
            bottomContainer.setOrientation(LinearLayout.VERTICAL);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            int height = new SystemBarConfig(activity).getNavigationBarHeight();
            params.bottomMargin = DeviceUtils.dip2px(activity, 50) + height;
            params.gravity = Gravity.BOTTOM;

            decorView.addView(bottomContainer, params);
            container = bottomContainer;
        }
        container.removeAllViews();
        new GuideModule(activity, container).showGuide();
        new HangUpModule(activity, container).showLayout();
    }
}
