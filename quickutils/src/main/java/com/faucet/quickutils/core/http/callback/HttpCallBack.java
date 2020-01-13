package com.faucet.quickutils.core.http.callback;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.faucet.quickutils.core.http.entity.BasicResponse;
import com.faucet.quickutils.core.http.serializer.BoolDefault0Adapter;
import com.faucet.quickutils.core.http.serializer.DoubleDefault0Adapter;
import com.faucet.quickutils.core.http.serializer.IntegerDefault0Adapter;
import com.faucet.quickutils.core.http.serializer.LongDefault0Adapter;
import com.zhy.http.okhttp.callback.Callback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * Created by mac on 16/3/17.
 */
public abstract class HttpCallBack<T> extends Callback<T> {

    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {

        if(response.code()!=200){
            throw new Exception("服务器出错啦");
        }
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            //如果用户写了泛型，就会进入这里，否者不会执行
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type beanType = parameterizedType.getActualTypeArguments()[0];
            if (beanType == String.class) {
                //如果是String类型，直接返回字符串
                return (T) response.body().string();
            } else {
                //请求是否被跳转
                if(response.isRedirect()){
                    new Exception("请检查网络连接是否正常");
                }
                //如果是 Bean List Map ，则解析完后返回
                String body = response.body().string();
                try {
                    T datas = buildGson().fromJson(body, beanType);
                    if (datas instanceof BasicResponse) {
                        BasicResponse httpResponse = (BasicResponse) datas;
                        Log.e("okhttp", "response code:" + httpResponse.getCode());
                    }
                    return datas;
                } catch (Exception e) {
                    Log.e("okhttp", "response:" + body);
                    Log.e("okhttp", "okhttp gson error:" + e.getMessage());
                    throw new Exception("数据解析异常");
                }
            }
        } else {
            //如果没有写泛型，直接返回Response对象
            return (T) response;
        }
    }

    public HttpCallBack() {
        super();
    }

    /**
     * 增加后台返回""和"null"的处理
     * 1.int=>0
     * 2.double=>0.00
     * 3.long=>0L
     *
     * @return
     */
    public Gson buildGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                .registerTypeAdapter(long.class, new LongDefault0Adapter())
                .registerTypeAdapter(Boolean.class, new BoolDefault0Adapter())
                .create();
        return gson;
    }
}
