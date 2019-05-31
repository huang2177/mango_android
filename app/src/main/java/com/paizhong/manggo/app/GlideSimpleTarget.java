package com.paizhong.manggo.app;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Des:
 * Created by huang on 2018/9/18 0018 10:49
 */
public class GlideSimpleTarget extends SimpleTarget<Drawable> {
    private OnGifReadyListener mListener;

    private GifDrawable gif;

    public GlideSimpleTarget(OnGifReadyListener listener) {
        mListener = listener;
    }

    @Override
    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
        if (resource instanceof GifDrawable) {
            gif = (GifDrawable) resource;
            if (mListener != null) {
                mListener.onGifReady(gif);
            }
        }
    }

    public void start() {
        if (gif != null) {
            if (gif.isRunning()) {
                return;
            }
            gif.start();
        }
    }

    public void stop() {
        if (gif != null) {
            if (gif.isRunning()) {
                gif.stop();
            }
        }
    }

    public interface OnGifReadyListener {
        void onGifReady(GifDrawable gif);
    }
}
