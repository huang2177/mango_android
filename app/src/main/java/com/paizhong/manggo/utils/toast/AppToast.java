package com.paizhong.manggo.utils.toast;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.paizhong.manggo.R;

/**
 * Created by zab on 2018/6/25 0025.
 */
public class AppToast {

    private static Toast toast;
    private static TextView textView;
    public static void register(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.app_toast_layout,null);
        textView = view.findViewById(R.id.tv_message);
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
    }


    public static void show(String text){
        if (TextUtils.isEmpty(text)){
            return;
        }
        if (toast !=null){
            textView.setText(text);
            toast.show();
        }
    }
}
