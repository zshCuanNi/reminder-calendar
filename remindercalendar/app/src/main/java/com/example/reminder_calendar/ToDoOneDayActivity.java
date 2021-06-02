package com.example.reminder_calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.reminder_calendar.toDoOneDay.ToDoOneDayRecyclerAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder_calendar.databinding.ActivityToDoOneDayBinding;

import java.util.ArrayList;

public class ToDoOneDayActivity extends AppCompatActivity {

    //private AppBarConfiguration appBarConfiguration;
    private ActivityToDoOneDayBinding binding;

    private ArrayList<String> dataSet = new ArrayList<>();
    private RecyclerView recyclerView;
    private ToDoOneDayRecyclerAdapter toDoOneDayRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityToDoOneDayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        //创建事件列表
        recyclerView = binding.recyclerView;

        RecyclerView.LayoutManager manager = new LinearLayoutManager(ToDoOneDayActivity.this);
        recyclerView.setLayoutManager(manager);
        int scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        recyclerView.scrollToPosition(scrollPosition);

        for(int i=0;i<1;i++)
            dataSet.add("item "+i);
        toDoOneDayRecyclerAdapter = new ToDoOneDayRecyclerAdapter(dataSet, ToDoOneDayActivity.this);
        recyclerView.setAdapter(toDoOneDayRecyclerAdapter);

        //添加一条事件
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ToDoOneDayActivity.this, ItemDetailActivity.class);
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
        String newText = bundle.getString("text");
        switch (requestCode){
            case 0:
                toDoOneDayRecyclerAdapter.changeData(position,newText);
                break;
            case 1:
                toDoOneDayRecyclerAdapter.addData(newText);
        }

    }

    //    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_to_do_one_day);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}