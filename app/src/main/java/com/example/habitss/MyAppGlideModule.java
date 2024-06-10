// src/main/java/com/example/habitss/MyAppGlideModule.java
package com.example.habitss;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import okhttp3.OkHttpClient;

@GlideModule
public final class MyAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
    }

    @Override
    public void registerComponents(Context context, Glide glide, com.bumptech.glide.Registry registry) {
        super.registerComponents(context, glide, registry);
        OkHttpClient client = new OkHttpClient.Builder().build();
        registry.replace(com.bumptech.glide.load.model.GlideUrl.class, java.io.InputStream.class, new com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader.Factory(client));
    }
}
