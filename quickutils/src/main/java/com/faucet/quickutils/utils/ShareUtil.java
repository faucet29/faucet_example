package com.faucet.quickutils.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class ShareUtil {

    public static void SystemShare(Context context, String text , Uri uri ){ //系统自带的分享，会通过你安装的软件，已列表方式显示出来，选择自己想要的分享方式
        try {
            Intent intent = new Intent("android.intent.action.SEND" );
            if( uri == null){
                intent.setType( "text/plain");
            } else{
                intent.setType( "image/*");
                intent.putExtra(Intent. EXTRA_STREAM, uri);
            }
            intent.putExtra(Intent. EXTRA_TEXT, text);
            intent.putExtra(Intent. EXTRA_SUBJECT, "分享到···" );
            intent.putExtra( "Kdescription", text);
            context.startActivity(Intent. createChooser(intent, "分享到···"));
        } catch (Exception e) {
            Toast. makeText(context, "SystemShare error", Toast.LENGTH_SHORT).show();
        }
    }
    
    public static void ShareWeibo(Context context, String text , Uri uri){//分享到新浪微博
        try {
            Intent intent = new Intent("android.intent.action.SEND" );
            ComponentName comp = new ComponentName("com.sina.weibo" , "com.sina.weibo.composerinde.ComposerDispatchActivity" );
            intent.setComponent( comp);
            if (uri == null) {
                intent.setType( "text/plain");
            } else {
                intent.setType( "image/*");
                intent.putExtra(Intent. EXTRA_STREAM, uri);
            }
            intent.putExtra(Intent. EXTRA_TEXT, text);
            intent.putExtra( "Kdescription", text);
            context.startActivity( intent);
        } catch (Exception e) {
            Toast. makeText(context, "分享出错，请确认是否安装微博客户端！" , Toast.LENGTH_SHORT).show();
        }
    }
    
    public static void ShareToQQqzone(Context context, String text , Uri uri){//分享到QQ空间
        try {
            Intent intent = new Intent("android.intent.action.SEND" );
            ComponentName comp = new ComponentName("com.qzone" , "com.qzonex.module.operation.ui.QZonePublishMoodActivity" );
            intent.setComponent( comp);
            if( uri == null){
                intent.setType( "text/plain");
            } else{
                intent.setType( "image/*");
                intent.putExtra(Intent. EXTRA_STREAM, uri);
            }
            intent.putExtra(Intent. EXTRA_TEXT, text);
            intent.putExtra( "Kdescription", text);
            context.startActivity( intent);
        } catch (Exception e) {
            Toast. makeText(context, "分享出错，请确认是否安装QQ空间！" , Toast.LENGTH_SHORT).show();
        }
    }
    
    public static void ShareToQQ(Context context, String text , Uri uri){//分享到QQ好友
        try {
            ComponentName comp = new ComponentName("com.tencent.mobileqq" , "com.tencent.mobileqq.activity.JumpActivity" );
            Intent intent = new Intent(Intent. ACTION_SEND);
            if( uri == null){
                intent.setType( "text/plain");
            } else{
                intent.setType( "image/*");
                intent.putExtra(Intent. EXTRA_STREAM, uri);
            }
            intent.putExtra(Intent. EXTRA_TEXT, text);
            intent.setFlags(Intent. FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent( comp);
            context.startActivity( intent);
        } catch (Exception e) {
            Toast. makeText(context, "分享出错，请确认是否安装QQ！" , Toast.LENGTH_SHORT).show();
        }
    }
    
    public static void ShareToWeixinFriendster(Context context, String title, Uri uri){//分享到微信朋友圈
        try {
            Intent intent = new Intent();
            ComponentName comp = new ComponentName("com.tencent.mm" , "com.tencent.mm.ui.tools.ShareToTimeLineUI" );
            intent.setComponent( comp);
            intent.setAction(Intent.ACTION_SEND);
            if( uri == null){
                intent.setType( "text/plain");
            } else{
                intent.setType( "image/*");
                intent.putExtra(Intent. EXTRA_STREAM, uri);
            }
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra( "Kdescription", title);
            context.startActivity( intent);
        } catch (Exception e) {
            Toast. makeText(context, "分享出错，请确认是否安装微信！" , Toast.LENGTH_SHORT).show();
        }
    }
    
    public static void ShareToWeixinFriend(Context context, String text , Uri url){//分享到微信好友
        try {
            Intent intent = new Intent();
            ComponentName comp = new ComponentName("com.tencent.mm" ,
                    "com.tencent.mm.ui.tools.ShareImgUI");
            intent.setComponent( comp);
            intent.setAction( "android.intent.action.SEND");
            if( url == null){
                intent.setType( "text/plain");
            } else{
                intent.setType( "image/*");
                intent.putExtra(Intent. EXTRA_STREAM, url);
            }
            intent.putExtra(Intent. EXTRA_TEXT, text);
            context.startActivity( intent);
        } catch (Exception e) {
            Toast. makeText(context, "分享出错，请确认是否安装微信！" , Toast.LENGTH_SHORT).show();
        }
    }

    public static void intentToWechat (Context context) {
        try {
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        } catch (Exception e) {
            AppUtils.openAppByPackageName(context, "com.tencent.mm");
        }
    }

    public static void intentToQQ (Context context) {
        try {
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.SplashActivity");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        } catch (Exception e) {
            AppUtils.openAppByPackageName(context, "com.tencent.mobileqq");
        }
    }
}
