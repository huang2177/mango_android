package com.paizhong.manggo.dialog.other;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.GlideApp;

/**
 * Created by zab on 2018/11/29 0029.
 */
public class ChatMsGuidedDialog extends Dialog{

    public ChatMsGuidedDialog(@NonNull Context context) {
        super(context, R.style.center_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_chat_msguided);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.width =  ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mWindow.setAttributes(params);
        ImageView imageView = findViewById(R.id.iv_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        GlideApp.with(getContext())
                .load(R.mipmap.chat_msg_yd)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
               .into(imageView);
    }
}
