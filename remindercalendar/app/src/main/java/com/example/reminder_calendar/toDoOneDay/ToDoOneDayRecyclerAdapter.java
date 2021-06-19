package com.example.reminder_calendar.toDoOneDay;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder_calendar.ItemDetailActivity;
import com.example.reminder_calendar.ListContent;
import com.example.reminder_calendar.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToDoOneDayRecyclerAdapter extends RecyclerView.Adapter<ToDoOneDayRecyclerAdapter.ViewHolder> {

    private static ArrayList<String> localTitleDataSet = new ArrayList<String>();
    private static ArrayList<String> localContentDataSet = new ArrayList<String>();
    private static ArrayList<String> localTimeDataSet = new ArrayList<String>();
    private static ArrayList<Integer> localIdDataSet = new ArrayList<>();
    static private Activity activity;
    private static String strDate;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView contentTextView;
        private final TextView titleTextView;
        private final TextView timeTextView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View.
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //传递数据，指示点击的是哪个item
                    Bundle bundle = new Bundle();
                    bundle.putString("content", (String) contentTextView.getText());
                    bundle.putString("title", (String) titleTextView.getText());
                    bundle.putString("time",(String) timeTextView.getText());
                    Integer position = getAdapterPosition();
                    bundle.putInt("position", position);
                    bundle.putString("date",strDate);
                    bundle.putInt("id",ToDoOneDayRecyclerAdapter.localIdDataSet.get(position));
                    bundle.putInt("requestCode", 0);
                    Intent intent = new Intent(activity, ItemDetailActivity.class);
                    intent.putExtra("bundle",bundle);
                    //requestCode为0，代表是编辑事件
                    activity.startActivityForResult(intent,0);
                }
            });
            contentTextView = (TextView) view.findViewById(R.id.contentTextView);
            titleTextView = (TextView) view.findViewById(R.id.titleTextView);
            timeTextView = (TextView) view.findViewById(R.id.timeTextView);
        }

        public TextView getContentTextView() {
            return contentTextView;
        }
        public TextView getTitleTextView() {
            return titleTextView;
        }
        public TextView getTimeTextView() {
            return timeTextView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     */
    public ToDoOneDayRecyclerAdapter(String strDate, Activity activity) {
        localTitleDataSet.clear();
        localContentDataSet.clear();
        localTimeDataSet.clear();
        localIdDataSet.clear();

        localTitleDataSet.addAll(ListContent.titleDataSet);
        localContentDataSet.addAll(ListContent.contentDataSet);
        localTimeDataSet.addAll(ListContent.timeDataSet);
        localIdDataSet.addAll(ListContent.idDataset);
        this.strDate = strDate;
        this.activity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.to_do_one_day_item , viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTitleTextView().setText(localTitleDataSet.get(position));
        viewHolder.getContentTextView().setText(localContentDataSet.get(position));
        viewHolder.getTimeTextView().setText(localTimeDataSet.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localTitleDataSet.size();
    }
    
    //用于增删数据的方法
    //移除数据
    public void removeData(int position) {
        localTitleDataSet.remove(position);
        localContentDataSet.remove(position);
        localTimeDataSet.remove(position);
        localIdDataSet.remove(position);
        notifyItemRemoved(position);
    }

    //新增数据
    public void addData(String title, String content, String time, Integer id) {
        localTitleDataSet.add(title);
        localContentDataSet.add(content);
        localTimeDataSet.add(time);
        localIdDataSet.add(id);
        notifyItemInserted(localTitleDataSet.size());
    }

    //更改某个位置的数据
    public void changeData(int position, String title, String content, String time, Integer id) {
        localTitleDataSet.set(position, title);
        localContentDataSet.set(position, content);
        localTimeDataSet.set(position, time);
        localIdDataSet.set(position, id);
        notifyItemChanged(position);
    }
}
