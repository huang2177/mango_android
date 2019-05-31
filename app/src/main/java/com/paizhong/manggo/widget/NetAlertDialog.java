package com.paizhong.manggo.widget;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import com.paizhong.manggo.R;

/**
 * Created by zab on 2018/3/27 0027.
 */

public class NetAlertDialog extends Dialog {

    private ImageView load_view;
    private Animation mAnimation;
    public NetAlertDialog(@NonNull Context context) {
        super(context, R.style.net_load_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_netaler);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = manage.getDefaultDisplay().getHeight();
        params.gravity = Gravity.CENTER;
        mWindow.setAttributes(params);
        //spinKitView = (SpinKitView)findViewById(R.id.load_view);
        load_view = (ImageView)findViewById(R.id.load_view);
        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_net_load_rotate);
        mAnimation.setInterpolator(new LinearInterpolator());//不停顿
    }

     public void showLoading(String text){
         show();
         if (load_view !=null){
             load_view.startAnimation(mAnimation);
         }
     }

     public void dismissLoading(){
         dismiss();
         if (load_view !=null){
             load_view.clearAnimation();
         }
     }
}
