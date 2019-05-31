package com.paizhong.manggo.dialog.signin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.ui.web.WebViewActivity;
import com.paizhong.manggo.utils.DeviceUtils;

/**
 * Created by zab on 2018/12/19 0019.
 */
public class SigninNewSucDialog extends Dialog{

    private ImageView ivImg1;
    private TextView tvMsg;

    public SigninNewSucDialog(@NonNull Context context) {
        super(context, R.style.center_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_signin_new_suc_layout);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;

        ivImg1 = findViewById(R.id.iv_img1);
        tvMsg = findViewById(R.id.tv_msg);

        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DeviceUtils.isFastDoubleClick()){
                    dismiss();
                }
            }
        });

        findViewById(R.id.tv_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DeviceUtils.isFastDoubleClick()){
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("url", Constant.H5_SHOP_MALL);
                    intent.putExtra("title", "积分商城");
                    intent.putExtra("noTitle", true);
                    getContext().startActivity(intent);
                    dismiss();
                }
            }
        });
    }


    public void showSucDialog(int score){
        show();
        ivImg1.setImageResource(R.mipmap.ic_sign_suc_img);
        SpannableStringBuilder style = new SpannableStringBuilder("获得"+score+"积分");
        try {
            style.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.color_F8E71C)), 2, String.valueOf(score).length()+2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }catch (Exception e){
            e.printStackTrace();
        }
        tvMsg.setText(style);
    }
}
