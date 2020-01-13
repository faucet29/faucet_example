package com.faucet.quickutils.core.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.faucet.quickutils.core.http.callback.HttpCallBack;
import com.faucet.quickutils.core.http.callback.LooperHttpCallback;
import com.faucet.quickutils.core.http.entity.BasicRequest;
import com.faucet.quickutils.core.http.impl.HttpInterface;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFileBuilder;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;

/**
 * Created by Faucet on 2016/8/30.
 * @详细API 参考https://github.com/hongyangAndroid/okhttputils
 */
public class HttpManager {

    /**
     * 组装header
     * @return
     */
    protected HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("Platform", "android");
        headers.put("Version", "1.0");
        return headers;
    }

    /**
     * 对象转为map
     * @param requestModel
     * @return
     */
    private HashMap<String,String> modelToMap(Object requestModel){
        HashMap<String, String> params = new HashMap<>();
        Field[] fields = requestModel.getClass().getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            String value = null;
            try {
                if (field.get(requestModel)!=null)
                {
                    if(field.get(requestModel) instanceof List){
                        List list = (List) field.get(requestModel);
                        for(int i=0;i<list.size();i++){
                            params.put(field.getName()+"["+i+"]",list.get(i).toString());
                        }
                    } else {
                        value = field.get(requestModel).toString();
                        if(!value.equals("null")){
                            params.put(field.getName(),value);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return params;
    }

    /**
     * get请求
     * @param requestModel  对象
     * @param callBack 为null，即为同步方法
     * @throws Exception
     */
    public void get(Context context, BasicRequest requestModel, HttpCallBack callBack){
        noNetError();
        try {
            OkHttpUtils.get().url(requestModel.getHttpRequestPath()).tag(context).headers(getHeaders()).params(modelToMap(requestModel)).build().execute(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get 请求
     * @param url
     * @param map
     * @param callBack 为null，即为同步方法
     * @throws Exception
     */
    public void get(Context context, String url, HashMap<String, String> map, HttpCallBack callBack) {
        noNetError();
        try {
            OkHttpUtils.get().url(url).tag(context).headers(getHeaders()).params(map).build().execute(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * post请求
     * @param requestModel
     * @param callBack
     * @throws Exception
     */
    public void postString(Context context, BasicRequest requestModel, HttpCallBack callBack){
        noNetError();
        try {
            OkHttpUtils.postString().url(requestModel.getHttpRequestPath()).tag(context).headers(getHeaders()).mediaType(okhttp3.MediaType.parse("application/json")).content(new Gson().toJson(requestModel)).build().execute(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Context context, BasicRequest requestModel, HttpCallBack callBack){
        noNetError();
        try {
            String json = new Gson().toJson(requestModel);
            okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(okhttp3.MediaType.parse("application/json"), json);
            OkHttpUtils.delete().url(requestModel.getHttpRequestPath()).tag(context).headers(getHeaders()).requestBody(requestBody).build().execute(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件post请求
     * @param url
     * @param file
     * @param callBack
     * @throws Exception
     */
    public void postFile(Context context, String url, File file, HttpCallBack callBack){
        noNetError();
        try {
            PostFileBuilder postFileBuilder = OkHttpUtils.postFile();
            postFileBuilder.url(url).tag(context).headers(getHeaders()).mediaType(MediaType.parse("application/octet-stream"));
            postFileBuilder.file(file).build().execute(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下載文件
     * @param url
     * @param callBack
     */
    public void downLoadFile(Context context, String url, FileCallBack callBack){
        noNetError();
        try {
            OkHttpUtils.get().url(url).tag(context).headers(getHeaders()).build().execute(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void put(Context context, BasicRequest requestModel, HttpCallBack callBack){
        noNetError();
        try {
            String json = new Gson().toJson(requestModel);
            okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(okhttp3.MediaType.parse("application/json"), json);
            OkHttpUtils.put().url(requestModel.getHttpRequestPath()).tag(context).headers(getHeaders()).requestBody(requestBody).build().execute(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据tag取消请求
     * @param tag
     * @使用方法 OkHttpUtils.cancelTag(this);//取消以Activity.this作为tag的请求
     */
    private void cancelRequestByTag(Object tag){
        noNetError();
        try {
            OkHttpUtils.getInstance().cancelTag(tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelRequestByUrl(String url){
        noNetError();
        try {
            RequestCall call = OkHttpUtils.get().url(url).build();
            call.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消所有的okhttp请求
     */
    public void cancelAll(){
        noNetError();
        try {
            OkHttpUtils.getInstance().getOkHttpClient().dispatcher().cancelAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downLoadImg(Context context, String url, BitmapCallback callBack){
        noNetError();
        try {
            OkHttpUtils.get().url(url).tag(context).headers(getHeaders()).build().execute(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void noNetError(){

    }

    public <T> void postOnLooper(Looper looper, HttpInterface<T> listener, T data, String error, boolean isScucess) {
        Looper myLooper = looper;
        if (myLooper == null) {
            myLooper = Looper.getMainLooper();
        }
        if(!isScucess){
            new Handler(myLooper, new LooperHttpCallback<>(listener, data, error, false)).sendEmptyMessage(1);
        }else{
            new Handler(myLooper, new LooperHttpCallback<>(listener, data, error, true)).sendEmptyMessage(0);
        }
    }
}
