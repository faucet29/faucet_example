package com.faucet.quickutils.core.manager;

import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.annotation.GlideModule;

@GlideModule
public class MyGlideModule extends AppGlideModule {

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
