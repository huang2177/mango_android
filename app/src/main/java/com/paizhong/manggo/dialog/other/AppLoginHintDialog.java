package com.paizhong.manggo.dialog.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.paizhong.manggo.R;

/**
 * Created by zab on 2018/4/16 0016.
 */

public class AppLoginHintDialog extends Dialog{
    private Context mContext;
    private TextView tvBtnLeft_login;
    private TextView tvBtnRight_login;
    private TextView tvContent_login;


    public AppLoginHintDialog(@NonNull Context context) {
        super(context, R.style.center_dialog);
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_app_login_layout);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        mWindow.setAttributes(params);

        tvContent_login = (TextView)findViewById(R.id.tv_content);
        tvBtnLeft_login = (TextView)findViewById(R.id.tv_btn_left);
        tvBtnRight_login = (TextView)findViewById(R.id.tv_btn_right);
    }



     //登录提示
     public void showLogin(String title, OnLeftClickListener listenerLeft, OnRightClickListener listenerRight){
         this.leftListener = listenerLeft;
         this.rightListener = listenerRight;
         show();
         tvContent_login.setText(title);
        //"请登录App后再操作" : "请登录交易所后再操作"
         tvBtnLeft_login.setOnClickListener( new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (leftListener !=null){
                     leftListener.onLeftClick();
                 }
                 dismiss();
             }
         });

         tvBtnRight_login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (rightListener !=null){
                     rightListener.onRightClick();
                 }
                 dismiss();
             }
         });
     }


     public OnLeftClickListener leftListener;
     public OnRightClickListener rightListener;


     public interface OnLeftClickListener{
         void onLeftClick();
     }
    public interface OnRightClickListener{
        void onRightClick();
    }
}
