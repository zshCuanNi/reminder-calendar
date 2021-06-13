package com.example.reminder_calendar;

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
    public static final OkHttpClient client = new OkHttpClient();
}
