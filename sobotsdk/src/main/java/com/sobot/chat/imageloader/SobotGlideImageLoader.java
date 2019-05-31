package com.sobot.chat.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

/**
 * 图片加载器  Glide
 */
public class SobotGlideImageLoader extends SobotImageLoader {

    @Override
    public void displayImage(Context context, final ImageView imageView, final String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height, final SobotDisplayImageListener listener) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(loadingResId)
                .error(failResId)
                .centerCrop();

        if (width != 0 || height != 0) {
            requestOptions.override(width, height);
        }
        RequestBuilder<Bitmap> builder = Glide.with(context).asBitmap().load(path).apply(requestOptions);
        builder.listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                if (listener != null) {
                    listener.onSuccess(imageView, path);
                }
                return false;
            }
        }).into(imageView);
    }

    @Override
    public void displayImage(Context context, final ImageView imageView, @DrawableRes int targetResId, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height, final SobotDisplayImageListener listener) {


        RequestOptions requestOptions = new RequestOptions()
                .placeholder(loadingResId)
                .error(failResId)
                .centerCrop();

        if (width != 0 || height != 0) {
            requestOptions.override(width, height);
        }
        RequestBuilder<Bitmap> builder = Glide.with(context).asBitmap().load(targetResId).apply(requestOptions);

        builder.listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                if (listener != null) {
                    listener.onSuccess(imageView, "");
                }
                return false;
            }
        }).into(imageView);
    }

}