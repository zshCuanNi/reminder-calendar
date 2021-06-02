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
import com.example.reminder_calendar.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ToDoOneDayRecyclerAdapter extends RecyclerView.Adapter<ToDoOneDayRecyclerAdapter.ViewHolder> {

    private ArrayList<String> localDataSet = new ArrayList<String>();
    static private Activity activity;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View.
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //传递数据，指示点击的是哪个item
                    Bundle bundle = new Bundle();
                    bundle.putString("text", (String) textView.getText());
                    Integer position = getAdapterPosition();
                    bundle.putInt("position", position);
                    Intent intent = new Intent(activity, ItemDetailActivity.class);
                    intent.putExtra("bundle",bundle);
                    activity.startActivityForResult(intent,0);
                }
            });
            textView = (TextView) view.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public ToDoOneDayRecyclerAdapter(List<String> dataSet, Activity activity) {
        localDataSet.addAll(dataSet);
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
        viewHolder.getTextView().setText(localDataSet.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
    
    //用于增删数据的方法
    //移除数据
    public void removeData(int position) {
        localDataSet.remove(position);
        notifyItemRemoved(position);
    }

    //新增数据
    public void addData(String text) {
        localDataSet.add(text);
        notifyItemInserted(localDataSet.size());
    }

    //更改某个位置的数据
    public void changeData(int position, String text) {
        localDataSet.set(position, text);
        notifyItemChanged(position);
    }
}
