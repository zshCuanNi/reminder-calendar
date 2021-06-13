package com.example.reminder_calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.reminder_calendar.databinding.ActivityItemDetailBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ItemDetailActivity extends AppCompatActivity {

    private ActivityItemDetailBinding binding;
    private TextView dateText;
    private EditText contentEditText;
    private EditText titleEditText;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //记录时间
    private Date oriDate;
    private Date newDate;

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            newDate = calendar.getTime();
            dateText.setText(simpleDateFormat.format(calendar.getTime()));
        }
    };
    //private FinishOnClickInterface finishOnClickInterface;
    private Integer position;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient okHttpClient = new OkHttpClient();
    private static final String SERVERURL = "http://10.0.2.2:8848";
    private static final String LOCALURL = "http://10.0.2.2:8848";

    private Integer requestCode;    //新增是1，编辑是0
    private static final int newMemoFlag = 1;
    private static final int editMemoFlag = 0;
    private static final int deleteMemoFlag = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dateText = binding.textviewSecondDate;
        //绑定日期点击
        dateText.setOnClickListener(v -> {
            new DatePickerDialog(
                    ItemDetailActivity.this,
                    onDateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        contentEditText = binding.edittextSecondContent;
        titleEditText = binding.edittextSecondTitle;
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        if(bundle!=null){
            String title = bundle.getString("title", "");
            String content = bundle.getString("content", "");
            String strDate = bundle.getString("date","null");
            requestCode = bundle.getInt("requestCode",0);

            if(!strDate.equals("null")) {
                try {
                    oriDate = simpleDateFormat.parse(strDate);
                    dateText.setText(strDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            position = bundle.getInt("position");
            titleEditText.setText(title);
            contentEditText.setText(content);
        }


        //完成按钮
        binding.fabComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishEditing(requestCode);
            }
        });

        //删除当前备忘按钮
        binding.fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(ItemDetailActivity.this);
                alertdialogbuilder.setMessage("删除当前备忘吗");
                alertdialogbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishEditing(deleteMemoFlag);
                    }
                });
                alertdialogbuilder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertdialogbuilder.create();
                alertdialogbuilder.show();
            }
        });
    }

    //截获返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishEditing(requestCode);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void finishEditing(int flag){
        //结束编辑，首先向后端发送请求信息

        JSONObject jsonObject = new JSONObject();
        String tmpURL;
        switch (flag){
            case newMemoFlag:

                try {
                    jsonObject.put("deadline", "2019-01-01");
                    jsonObject.put("detail", "detail test");
                    jsonObject.put("headline", "headline test");
                    jsonObject.put("username", "username test");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tmpURL = LOCALURL+"/api/newMemo";
                break;

            case editMemoFlag:
                try {
                    jsonObject.put("deadline", "2019-01-01");
                    jsonObject.put("detail", "detail test");
                    jsonObject.put("headline", "headline test");
                    jsonObject.put("username", "username test");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tmpURL = LOCALURL+"/api/newMemo";
                break;
            case deleteMemoFlag:
                try {
                    jsonObject.put("deadline", "2019-01-01");
                    jsonObject.put("detail", "detail test");
                    jsonObject.put("headline", "headline test");
                    jsonObject.put("username", "username test");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tmpURL = LOCALURL+"/api/newMemo";
                break;
            default:
                tmpURL = LOCALURL+"/api/newMemo";
        }

        getDataFromPost(tmpURL, jsonObject.toString());
        Bundle bundle = new Bundle();
        if(position!=null)
            bundle.putInt("position", position);
        bundle.putString("title", titleEditText.getText().toString());
        bundle.putString("content", contentEditText.getText().toString());
        Intent intent = new Intent();
        intent.putExtra("bundle", bundle);
        setResult(0, intent);
        finish();
    }


    /*
    *后端交互部分
    *
    */
    //申请动态内容
    private Handler getHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            try {
                JSONObject jsonObject = new JSONObject((String)msg.obj);
                String code = jsonObject.getString("code");
                Log.e("http",code);
            } catch (JSONException e) {
                Log.e("failhttp","fail");
                e.printStackTrace();
            }
            //super.handleMessage(msg);
            return true;
        }
    });

    //从post获取数据
    private void getDataFromPost(String url, String json) {
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
    private String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            return response.body().string();
        }
    }
    public void getDataFromGet(String url, int what) {
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
    private String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.e("begin", "newCall");
        Response response = okHttpClient.newCall(request).execute();
        Log.e("end", "newCall");
        return response.body().string();
    }
}