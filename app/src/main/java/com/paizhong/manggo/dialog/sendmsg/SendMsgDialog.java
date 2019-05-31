package com.paizhong.manggo.dialog.sendmsg;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.base.BaseDialog;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.utils.AndroidUtil;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.SpUtil;

/**
 * 交易 消息发送
 * Created by zab on 2018/11/28 0028.
 */
public class SendMsgDialog extends BaseDialog<SendMsgPresenter> implements SendMsgContract.View{


    private EditText etInput;

    public SendMsgDialog(@NonNull BaseActivity context) {
        super(context, R.style.translucent_theme);
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_send_msg);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);

        View vLine = findViewById(R.id.v_line);
        etInput = findViewById(R.id.et_input);
        TextView tvSend = findViewById(R.id.tv_send);
        AndroidUtil.setEmojiFilter(etInput);
        vLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                DeviceUtils.hintKeyboard(mContext);
            }
        });

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = etInput.getText().toString();
                if (TextUtils.isEmpty(msg)){
                    showErrorMsg("发送弹幕不能为空",null);
                    return;
                }
                msg = msg.trim();
                if (TextUtils.isEmpty(msg)){
                    showErrorMsg("发送弹幕不能为空",null);
                    return;
                }
                if (msg.length() > 15){
                    showErrorMsg("发送弹幕长度不能大于15",null);
                    return;
                }
                if (isFast()){
                    showErrorMsg("发送弹幕间隔时间不能少于5秒",null);
                }else {
                    mPresenter.getMsgProducer(AppApplication.getConfig().getMobilePhone(),msg, SpUtil.getString(Constant.USER_KEY_NICKNAME));
                }
            }
        });
    }


    private long mLastTime;
    private  boolean isFast() {
        long time = System.currentTimeMillis();
        long timeD = time - mLastTime;
        if (0 < timeD && timeD < 5000) {
            return true;
        }
        this.mLastTime = time;
        return false;
    }


    @Override
    public void dismiss() {
        if (etInput !=null){
            etInput.setText("");
        }
        super.dismiss();
    }

    @Override
    public void show() {
        super.show();
        if (etInput !=null){
            DeviceUtils.showSoftInPut(mContext,etInput,false);
        }
    }

    @Override
    public void bindMsgProducer() {
        if (mContext !=null && !mContext.isFinishing()){
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    etInput.setText("");
                    showErrorMsg("发送成功",null);
                    dismiss();
                }
            });
        }
    }
}
