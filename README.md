# 快捷开发及常用库

#### 介绍
本工程包含快捷开发库QuickUtil, 仿微信、钉钉群组头像库GroupHeadViewLib, 及部分第三方框架的高级使用案例测试（greendao 3.2.2 使用greendao-generator-3.2.2 实现快速建库及自定义关联表）

#### 软件架构
**快捷开发库QuickUtil包含**  
常用Utils、APP新手操作引导、上下刷新加载、集成了滑动返回及权限请求等快捷开发的baseactivity、OKhttp请求框架封装、glide图片加载封装等

**仿微信、钉钉群组头像库GroupHeadViewLib**  
参考CombineBitmap https://github.com/SheHuan/CombineBitmap 修改整理  
在其基础上修改图片加载为glide加载，支持androidX的使用，支持图文头像。  
![image](https://github.com/faucet29/faucet_example/blob/master/image/WX20200113-162807%402x.png)  
![image](https://github.com/faucet29/faucet_example/blob/master/image/WX20200114-105050%402x.png)
![image](https://github.com/faucet29/faucet_example/blob/master/image/WX20200114-105101%402x.png)  

#### 安装教程

无

#### 使用说明

**GroupHeadViewLib使用**  
```
CombineBitmap.init(context)
    .setLayoutManager() // 必选， 设置图片的组合形式，支持WechatLayoutManager、DingLayoutManager
    .setSize() // 必选，组合后Bitmap的尺寸，单位dp
    .setGap() // 单个图片之间的距离，单位dp，默认0dp
    .setGapColor() // 单个图片间距的颜色，默认白色
    .setPlaceholder() // 单个图片加载失败的默认显示图片
    .setUrls() // 要加载的图片url数组，也可直接传文字
    .setBitmaps() // 要加载的图片bitmap数组
    .setResourceIds() // 要加载的图片资源id数组
    .setImageView() // 直接设置要显示图片的ImageView
    // 设置“子图片”的点击事件，需使用setImageView()，index和图片资源数组的索引对应
    .setOnSubItemClickListener(new OnSubItemClickListener() {
        @Override
        public void onSubItemClick(int index) {

        }
    })
    // 加载进度的回调函数，如果不使用setImageView()方法，可在onComplete()完成最终图片的显示
    .setProgressListener(new ProgressListener() {
        @Override
        public void onStart() {

        }

        @Override
        public void onComplete(Bitmap bitmap) {

        }
    })
    .build();
```
