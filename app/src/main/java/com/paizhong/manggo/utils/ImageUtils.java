package com.paizhong.manggo.utils;

import android.content.Context;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.GlideApp;
import com.paizhong.manggo.app.GlideSimpleTarget;

import java.io.File;

/**
 * Created by zab on 2018/8/16 0016.
 */
public class ImageUtils {

    public static void display(String imageUrl, ImageView imageView, Integer errorImgResouce, Integer placeholder) {
        if (imageView == null || imageView.getContext() == null) {
            return;
        }
        GlideApp.with(imageView.getContext())
                .load(imageUrl)
                .centerCrop()
                .placeholder(placeholder)
                .error(errorImgResouce)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);
    }


    public static void display(String imageUrl, ImageView imageView, Integer errorImgResouce) {
        if (imageView == null || imageView.getContext() == null) {
            return;
        }
        GlideApp.with(imageView.getContext())
                .load(imageUrl)
                .centerCrop()
                .placeholder(errorImgResouce)
                .error(errorImgResouce)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);
    }
    public static void display(String imageUrl, ImageView imageView) {
        if (imageView == null || imageView.getContext() == null) {
            return;
        }
        GlideApp.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(R.mipmap.other_empty)
                .error(R.mipmap.other_empty)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);
    }


    public static void displaySplash(String imageUrl, ImageView imageView) {
        if (imageView == null || imageView.getContext() == null) {
            return;
        }
        GlideApp.with(imageView.getContext())
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);
    }

    public static void display(byte[] bitmapArray, ImageView imageView, Integer errorImgResouce) {
        if (errorImgResouce == null) {
            errorImgResouce = R.mipmap.other_empty;
        }
        if (imageView ==null || imageView.getContext()==null){
            return;
        }
        GlideApp.with(imageView.getContext())
                .load(bitmapArray)
                .centerCrop()
                .placeholder(errorImgResouce)
                .error(errorImgResouce)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);
    }


    //将base64转Byte
    public static byte[] base64ToByte(String string) {
        return Base64.decode(string.split(",")[1], Base64.DEFAULT);
    }

    public static void display(File file, ImageView imageView, Integer errorImgResouce) {
        if (errorImgResouce == null) {
            errorImgResouce = R.mipmap.other_empty;
        }

        GlideApp.with(imageView.getContext())
                .load(file)
                .placeholder(errorImgResouce)
                .error(errorImgResouce)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);
    }

    /**
     * 利用Glide播放Gif
     */
    public static void displayGif(Context context, String gifResId, GlideSimpleTarget target) {
        GlideApp.with(context)
                .load(gifResId)
                .placeholder(R.mipmap.other_empty)
                .error(R.mipmap.other_empty)
                .into(target);
    }

    /**
     * 利用Glide播放Gif
     */
    public static void clear(Context context, ImageView view, GlideSimpleTarget target) {
        GlideApp.with(context).clear(view);
        GlideApp.with(context).clear(target);
    }
}
