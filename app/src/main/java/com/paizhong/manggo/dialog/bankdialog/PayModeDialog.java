package com.paizhong.manggo.dialog.bankdialog;

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
import com.paizhong.manggo.widget.wheelview.ArrayWheelAdapter;
import com.paizhong.manggo.widget.wheelview.WheelView;

/**
 * 选择支付方式
 * Created by zab on 2018/4/29 0029.
 */

public class PayModeDialog extends Dialog{
    private Context mContext;

    private String [] titles;
    private String [] codes;
    public PayModeDialog(@NonNull Context context ,String [] titles,String [] codes) {
        super(context, R.style.translucent_theme);
        this.mContext = context;
        this.titles = titles;
        this.codes = codes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay_mode);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        final WheelView wv_chose_pay = (WheelView)findViewById(R.id.wv_chose_pay);
        TextView tv_btn = (TextView)findViewById(R.id.tv_btn);

        tv_title.setText("请选择绑定方式");
        wv_chose_pay.setVisibleItems(3);
        wv_chose_pay.setViewAdapter(new ArrayWheelAdapter<>(mContext,titles));

        tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (listener !=null){
                    int pCurrent = wv_chose_pay.getCurrentItem();
                    listener.onClick(codes[pCurrent],titles[pCurrent]);
                }
            }
        });
    }

    public DialogChoosePayListener listener;
    public void setDialogChoosePayListener(DialogChoosePayListener listener){
        this.listener = listener;
    }
}
