package com.faucet.quickutils.core.manager;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.annotation.GlideModule;

import androidx.annotation.NonNull;

@GlideModule
public class MyGlideModule extends AppGlideModule {

//  @Override
//  public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
//    int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
//    int diskCacheSizeBytes = 1024 * 1024 * 100;  //100 MB
//    builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes))
//            .setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));
//  }

  /**
   * 禁用清单解析
   * 这样可以改善 Glide 的初始启动时间，并避免尝试解析元数据时的一些潜在问题。
   *
   * @return
   */
  @Override
  public boolean isManifestParsingEnabled() {
    return false;
  }
}
