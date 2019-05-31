package com.paizhong.manggo.app;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by zab on 2018/8/6 0006.
 */

@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
