package com.faucet.quickutils.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author: zhuangmx
 *     time  : 2016/8/2
 *     desc  : JSON相关工具类
 * </pre>
 */
public class JSONUtils {

    private JSONUtils() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private static JSONObject object = new JSONObject();


    public static JSONObject toJson(String json) throws JSONException {
        return object.getJSONObject(json);
    }

    /**
     * 对象装JSON字符串
     * @param object
     * @return
     */
    public static String toJson(Object object){
        return gson.toJson(object);
    }

    /**
     * json字符串转对象
     * @param json
     * @param tClass
     * @return
     */
    public static <T>T fromJson(String json, Class<T> tClass){

        return gson.fromJson(json,tClass);
    }

    /**
     * json字符串转List
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T>T fromJson(String json, Type type){
        return gson.fromJson(json,type);
    }


}
