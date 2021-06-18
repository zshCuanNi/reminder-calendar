package com.example.reminder_calendar;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Cookie;
import okhttp3.CookieJar;

public class HttpServer {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static final String SERVERURL = "http://10.0.2.2:8848";
    public static final String LOCALURL = "http://10.0.2.2:8848";
    public static final String CURRENTURL = LOCALURL;
    public static final OkHttpClient okHttpClient = new OkHttpClient();
    //从post获取数据
    public static void getDataFromPost(String url, String json, Handler getHandler) {
        //Log.e("TAG", "Start getDataFromGet()");
        new Thread(){
            @Override
            public void run() {
                super.run();
                //Log.e("TAG", "new thread run.");
                try {
                    String result = post(url, json);
                    Log.e("result", result);
                    Message msg = Message.obtain();
                    msg.obj = result;
                    //msg.what = what;
                    getHandler.sendMessage(msg);
                } catch (java.io.IOException IOException) {
                    Log.e("TAG", "post failed.");
                    Log.e("exception", IOException.getLocalizedMessage());
                }
            }
        }.start();
    }
    /**
     * Okhttp的post请求
     * @param url
     * @param json
     * @return 服务器返回的字符串
     * @throws IOException
     */
    private static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            return response.body().string();
        }
    }
    public static void getDataFromGet(String url, int what, Handler getHandler) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    String result = get(url);
                    Log.e("TAG", result);
                    Message msg = Message.obtain();
                    msg.obj = result;
                    msg.what = what;
                    getHandler.sendMessage(msg);
                } catch (java.io.IOException IOException) {
                    Log.e("TAG", "get failed.");
                }
            }
        }.start();
    }
    /**
     * Okhttp的get请求
     * @param url
     * @return 服务器返回的字符串
     * @throws IOException
     */
    private static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.e("begin", "newCall");
        Response response = okHttpClient.newCall(request).execute();
        Log.e("end", "newCall");
        return response.body().string();
    }
}
