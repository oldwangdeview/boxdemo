package com.oldwang.boxdemo.http;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class OkHttpInterceptor implements Interceptor {

    public static final String TAG = "OkHttpUtils";
    private String tag;
    private boolean showResponse;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public OkHttpInterceptor(String tag, boolean showResponse)
    {
        if (TextUtils.isEmpty(tag))
        {
            tag = TAG;
        }
        this.showResponse = showResponse;
        this.tag = tag;
    }

    public OkHttpInterceptor(String tag)
    {
        this(tag, false);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        //获取到request
        Request request = chain.request();

        //获取到方法
        String method = request.method();

        try {


            RequestBody requestBody = request.body();
            HashMap<String, Object> rootMap = new HashMap<>();
            if (requestBody instanceof FormBody) {
                for (int i = 0; i < ((FormBody) requestBody).size(); i++) {
                    rootMap.put(((FormBody) requestBody).encodedName(i), ((FormBody) requestBody).encodedValue(i));
                }
            } else {
                //buffer流
               String temp =  bodyToString(request);
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                String oldParamsJson = buffer.readUtf8();
                rootMap = new Gson().fromJson(oldParamsJson, HashMap.class);  //原始参数
                String newJsonParams = new Gson().toJson(rootMap);  //装换成json字符串

                request = request.newBuilder().post(RequestBody.create(JSON, newJsonParams)).build();
            }

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        //最后通过chain.proceed(request)进行返回
        return chain.proceed(request);
    }



    private boolean isText(MediaType mediaType)
    {
        if (mediaType.type() != null && mediaType.type().equals("text"))
        {
            return true;
        }
        if (mediaType.subtype() != null)
        {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")
                    )
                return true;
        }
        return false;
    }

    private String bodyToString(final Request request)
    {
        try
        {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e)
        {
            return "something error when show requestBody.";
        }
    }
}
