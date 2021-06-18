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

    private ArrayList<String> titleDataSet;
    private ArrayList<String> contentDataSet;
    private ArrayList<String> timeDataSet;
    private RecyclerView recyclerView;
    private ToDoOneDayRecyclerAdapter toDoOneDayRecyclerAdapter;
    private Toolbar toolbar;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient okHttpClient = HttpServer.okHttpClient;
    private static String date = "2000-01-01";
    private static String time = "00:00";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        if(bundle!=null){
            date = bundle.getString("date","2000-01-01");
            time = bundle.getString("time","00:00");
        }


        binding = ActivityToDoOneDayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Log.e("date",date);
        getSupportActionBar().setTitle(date);

        timeDataSet = ListContent.timeDataSet;
        titleDataSet = ListContent.titleDataSet;
        contentDataSet = ListContent.contentDataSet;

        //创建事件列表
        recyclerView = binding.recyclerView;

        RecyclerView.LayoutManager manager = new LinearLayoutManager(ToDoOneDayActivity.this);
        recyclerView.setLayoutManager(manager);
        int scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        recyclerView.scrollToPosition(scrollPosition);


        for(int i=0;i<0;i++) {
            titleDataSet.add("title" + i);
            contentDataSet.add("content"+i);
            timeDataSet.add("hh:mm"+i);
        }
        toDoOneDayRecyclerAdapter = new ToDoOneDayRecyclerAdapter(titleDataSet, contentDataSet, timeDataSet, date, ToDoOneDayActivity.this);
        recyclerView.setAdapter(toDoOneDayRecyclerAdapter);

        //添加一条事件
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("date",date);
                bundle.putString("time",time);
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
//            public void onClick(View view)place with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show( {
//                Snackbar.make(view, "Re);
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
        String newTime = bundle.getString("time");
        switch (resultCode){
            case FlagValues.editMemoFlag:
                toDoOneDayRecyclerAdapter.changeData(position,newTitle,newContent,newTime);
                break;
            case FlagValues.newMemoFlag:
                toDoOneDayRecyclerAdapter.addData(newTitle, newContent,newTime);
                break;
            case FlagValues.deleteMemoFlag:
                toDoOneDayRecyclerAdapter.removeData(position);
                break;
            case FlagValues.doNothingFlag:
                break;
            default:
        }

    }


    //    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_to_do_one_day);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}