package com.example.reminder_calendar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.reminder_calendar.toDoOneDay.ToDoOneDayRecyclerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder_calendar.databinding.ActivityToDoOneDayBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ToDoOneDayActivity extends AppCompatActivity {

    //private AppBarConfiguration appBarConfiguration;
    private ActivityToDoOneDayBinding binding;

    private ArrayList<String> titleDataSet = new ArrayList<>();
    private ArrayList<String> contentDataSet = new ArrayList<>();
    private RecyclerView recyclerView;
    private ToDoOneDayRecyclerAdapter toDoOneDayRecyclerAdapter;
    private Toolbar toolbar;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient okHttpClient = new OkHttpClient();
    private static final String SERVERURL = "http://10.0.2.2:8848";
    private static final String LOCALURL = "http://10.0.2.2:8848";
    private static String date = "2000-01-01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        if(bundle!=null){
            date = bundle.getString("date","2000-01-01");
        }


        binding = ActivityToDoOneDayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Log.e("date",date);
        getSupportActionBar().setTitle(date);


        //创建事件列表
        recyclerView = binding.recyclerView;

        RecyclerView.LayoutManager manager = new LinearLayoutManager(ToDoOneDayActivity.this);
        recyclerView.setLayoutManager(manager);
        int scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        recyclerView.scrollToPosition(scrollPosition);


        for(int i=0;i<10;i++) {
            titleDataSet.add("title" + i);
            contentDataSet.add("content"+i);
        }
        toDoOneDayRecyclerAdapter = new ToDoOneDayRecyclerAdapter(titleDataSet, contentDataSet, ToDoOneDayActivity.this);
        recyclerView.setAdapter(toDoOneDayRecyclerAdapter);

        //添加一条事件
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("date","2018-01-01");
                bundle.putInt("requestCode", 1);
                Intent intent = new Intent(ToDoOneDayActivity.this, ItemDetailActivity.class);
                intent.putExtra("bundle",bundle);
                //requestCode为1，代表是新增事件
                startActivityForResult(intent,1);
            }
        });




//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_to_do_one_day);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getBundleExtra("bundle");
        Integer position = bundle.getInt("position");
        String newContent = bundle.getString("content");
        String newTitle = bundle.getString("title");
        switch (requestCode){
            case 0:
                toDoOneDayRecyclerAdapter.changeData(position,newTitle,newContent);
                break;
            case 1:
                toDoOneDayRecyclerAdapter.addData(newTitle, newContent);
        }

    }

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
    //    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_to_do_one_day);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}