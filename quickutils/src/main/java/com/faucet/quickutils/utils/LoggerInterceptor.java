package com.faucet.quickutils.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LoggerInterceptor implements Interceptor {
    public static final String TAG = "OkHttpUtils";
    private String tag;
    private boolean showResponse;

    public LoggerInterceptor(String tag, boolean showResponse) {
        if (TextUtils.isEmpty(tag)) {
            tag = "OkHttpUtils";
        }

        this.showResponse = showResponse;
        this.tag = tag;
    }

    public LoggerInterceptor(String tag) {
        this(tag, false);
    }

    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        this.logForRequest(request);
        Response response = chain.proceed(request);
        return this.logForResponse(response);
    }

    private Response logForResponse(Response response) {
        try {
            if (this.showResponse) {
                Log.d(this.tag, "========response'log=======");
                Response.Builder builder = response.newBuilder();
                Response clone = builder.build();
                Log.d(this.tag, "url : " + clone.request().url());
                Log.d(this.tag, "code : " + clone.code());
                Log.d(this.tag, "protocol : " + clone.protocol());
                if (!TextUtils.isEmpty(clone.message())) {
                    Log.d(this.tag, "message : " + clone.message());
                }

                ResponseBody body = clone.body();
                if (body != null) {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
                        Log.d(this.tag, "responseBody's contentType : " + mediaType.toString());
                        if (this.isText(mediaType)) {
                            String resp = body.string();
                            showLargeLog(this.tag, "responseBody's content : \n" + JsonFormat.format(resp));
                            body = ResponseBody.create(mediaType, resp);
                            Log.d(this.tag, "========response'log=======end");
                            return response.newBuilder().body(body).build();
                        }

                        Log.d(this.tag, "responseBody's content :  maybe [file part] , too large too print , ignored!");
                    }
                }
            }
        } catch (Exception var7) {
            ;
        }

        return response;
    }

    private void logForRequest(Request request) {
        try {
            if (this.showResponse) {
                String url = request.url().toString();
                Headers headers = request.headers();
                Log.d(this.tag, "========request'log=======");
                Log.d(this.tag, "method : " + request.method());
                Log.d(this.tag, "url : " + url);
                if (headers != null && headers.size() > 0) {
                    Log.d(this.tag, "headers : " + headers.toString());
                }

                RequestBody requestBody = request.body();
                if (requestBody != null) {
                    MediaType mediaType = requestBody.contentType();
                    if (mediaType != null) {
                        Log.d(this.tag, "requestBody's contentType : " + mediaType.toString());
                        if (this.isText(mediaType)) {
                            Log.d(this.tag, "requestBody's content : \n" + JsonFormat.format(this.bodyToString(request)));
                        } else {
                            Log.d(this.tag, "requestBody's content :  maybe [file part] , too large too print , ignored!");
                        }
                    }
                }

                Log.d(this.tag, "========request'log=======end");
            }
        } catch (Exception var6) {
            ;
        }

    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        } else {
            return mediaType.subtype() != null && (mediaType.subtype().equals("json") || mediaType.subtype().equals("xml") || mediaType.subtype().equals("html") || mediaType.subtype().equals("webviewhtml"));
        }
    }

    private String bodyToString(Request request) {
        try {
            Request copy = request.newBuilder().build();
            Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException var4) {
            return "something error when show requestBody.";
        }
    }


    /**
     * 分段打印出较长log文本
     * @param msg  打印文本
     */
    private void showLargeLog(String TAG, String msg){
        int max_str_length = 2001 - TAG.length();
        while (msg.length() > max_str_length) {
            Log.d(TAG, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.d(TAG,msg);
    }
}