package com.paizhong.manggo.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

/**
 * Created by zab on 2018/5/9 0009.
 */

public class ViewHelper {

    public static <T extends TextView> void safelySetText(T view, CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            view.setText(text);
        } else {
            view.setText("");
        }
    }


    public static <T extends TextView> void safelySetTextVisib(T view, CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 设置textView左边的图片
     *
     * @param view
     * @param resId
     * @param <T>
     */
    public static <T extends TextView> void drawableLeft(T view, int resId) {
        if (resId == 0) {
            return;
        }
        view.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
    }

    /**
     * 设置textView右边的图片
     *
     * @param view
     * @param resId
     * @param <T>
     */
    public static <T extends TextView> void drawableRight(T view, int resId) {
        if (resId == 0) {
            return;
        }
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);

    }

    public static Bitmap getResBitmap(Resources resources ,int imageRes){
        if (resources !=null){
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
            opt.inPurgeable = true;
            opt.inInputShareable = true;
            //获取资源图片
            InputStream is = resources.openRawResource(imageRes);
            return BitmapFactory.decodeStream(is, null, opt);
        }else {
            return null;
        }
    }

    //销毁背景图片
    public static <T extends View> void recycleBackground(T view) {
        if (view !=null){
            BitmapDrawable bitmapDrawable = null;
            try{
                bitmapDrawable = (BitmapDrawable)view.getBackground();
            }catch (ClassCastException e){
                if (view.getBackground() !=null){
                    view.getBackground().setCallback(null);
                }
            }
            if (bitmapDrawable !=null){
                view.setBackgroundResource(0);
                bitmapDrawable.setCallback(null);
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if (bitmap !=null && !bitmap.isRecycled()){
                    bitmap.recycle();
                    bitmapDrawable = null;
                    bitmap = null;
                }
            }
        }
    }

    //设置背景图片
    public static <T extends View> void setBackground(T view , Resources resources,int resourcesId){
       if (view !=null){
           Bitmap bitmap =  getResBitmap(resources,resourcesId);
           if (bitmap !=null && !bitmap.isRecycled()){
               BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, bitmap);
               if (bitmapDrawable !=null){
                   view.setBackground(bitmapDrawable);
               }
           }
       }
    }


    public static <T extends ImageView> void recycleImageView(T view){
        BitmapDrawable bitmapDrawable = null;
        try{
            bitmapDrawable = (BitmapDrawable)view.getDrawable();
        }catch (ClassCastException e){
            if (view.getDrawable() !=null){
                view.getDrawable().setCallback(null);
            }
        }
        if (bitmapDrawable !=null){
            bitmapDrawable.setCallback(null);
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap !=null && !bitmap.isRecycled()){
                bitmap.recycle();
                bitmapDrawable = null;
                bitmap = null;
            }
        }
    }


    public static <T extends ImageView> void setImageView(T view , Resources resources,int resourcesId){
        if (view !=null){
            Bitmap bitmap = getResBitmap(resources,resourcesId);
            if (bitmap !=null && !bitmap.isRecycled()){
                view.setImageBitmap(bitmap);
            }
        }
    }
}
