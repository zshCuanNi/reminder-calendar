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
    private EditText timeEditText;
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
            dateText.setText(simpleDateFormat.format(newDate));
        }
    };
    //private FinishOnClickInterface finishOnClickInterface;
    private Integer position;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient okHttpClient = HttpServer.okHttpClient;

    private Integer id=0;

    private Integer requestCode;    //新增是1，编辑是0
    private Integer resultCode;     //新增是1，编辑是0，删除新增(什么都不做)是2，删除已有是3


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
                int code = jsonObject.getInt("code");
                if(code==200){
                    String data = jsonObject.getString("data");
                    if(data.contains("添加成功")) {
                        Log.e("http add data",data.substring(data.length()-1));
                        id = Integer.parseInt(data.substring(data.length()-1));
                        Log.e("http id", "" + id);
                    }
                }
            } catch (JSONException e) {
                Log.e("failhttp","fail");
                e.printStackTrace();
            }
            //super.handleMessage(msg);
            return true;
        }
    });


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
        timeEditText = binding.edittextSecondTime;
        titleEditText = binding.edittextSecondTitle;
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        if(bundle!=null){
            String title = bundle.getString("title", "");
            String content = bundle.getString("content", "");
            String time = bundle.getString("time","");
            String strDate = bundle.getString("date","null");
            id = bundle.getInt("id");
            requestCode = bundle.getInt("requestCode",0);

            if(!strDate.equals("null")) {
                try {
                    oriDate = simpleDateFormat.parse(strDate);
                    newDate = simpleDateFormat.parse(strDate);
                    dateText.setText(strDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            position = bundle.getInt("position");
            titleEditText.setText(title);
            contentEditText.setText(content);
            timeEditText.setText(time);
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
                        finishEditing(FlagValues.deleteMemoFlag);
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
            case FlagValues.newMemoFlag:
                if(titleEditText.getText().equals("") && contentEditText.getText().equals("") && timeEditText.getText().equals("")){
                    resultCode = FlagValues.doNothingFlag;
                    break;
                }
                try {
                    jsonObject.put("deadline", dateText.getText()+" "+timeEditText.getText());
                    jsonObject.put("detail", contentEditText.getText());
                    jsonObject.put("headline", titleEditText.getText());
                    jsonObject.put("username", ListContent.username);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tmpURL = HttpServer.LOCALURL+"/api/newMemo";
                HttpServer.getDataFromPost(tmpURL, jsonObject.toString(), getHandler);
                resultCode = requestCode;
                break;

            case FlagValues.editMemoFlag:
                try {
                    jsonObject.put("deadline", dateText.getText()+ " " + timeEditText.getText());
                    jsonObject.put("detail", contentEditText.getText());
                    jsonObject.put("headline", titleEditText.getText());
                    jsonObject.put("id", id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tmpURL = HttpServer.LOCALURL+"/api/editMemo";
                HttpServer.getDataFromPost(tmpURL, jsonObject.toString(), getHandler);
                resultCode = requestCode;
                if(newDate!=null && !newDate.equals(oriDate))
                    resultCode = FlagValues.deleteMemoFlag;
                break;
            case FlagValues.deleteMemoFlag:
                try {
                    jsonObject.put("id", id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tmpURL = HttpServer.LOCALURL+"/api/newMemo";
                HttpServer.getDataFromPost(tmpURL, jsonObject.toString(),getHandler);
                if(requestCode==FlagValues.newMemoFlag){
                    resultCode = FlagValues.doNothingFlag;
                }else{
                    resultCode = FlagValues.deleteMemoFlag;
                }
                break;
            default:
        }
        Bundle bundle = new Bundle();
        if(position!=null)
            bundle.putInt("position", position);
        bundle.putString("title", titleEditText.getText().toString());
        bundle.putString("content", contentEditText.getText().toString());
        bundle.putString("time", timeEditText.getText().toString());
        bundle.putInt("id",id);
        Intent intent = new Intent();
        intent.putExtra("bundle", bundle);
        setResult(resultCode, intent);
        finish();
    }

}