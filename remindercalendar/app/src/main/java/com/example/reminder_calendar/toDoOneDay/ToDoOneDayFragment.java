package com.example.reminder_calendar.toDoOneDay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminder_calendar.R;
import com.example.reminder_calendar.databinding.FragmentToDoOneDayBinding;

public class ToDoOneDayFragment extends Fragment {

    private FragmentToDoOneDayBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentToDoOneDayBinding.inflate(inflater, container, false);

        //创建事件列表
        RecyclerView recyclerView = binding.recyclerView;

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        int scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        recyclerView.scrollToPosition(scrollPosition);

        String[] dataSet = new String[50];
        for(int i=0;i<50;i++)
            dataSet[i] = "item " + i;
        ToDoOneDayRecyclerAdapter toDoOneDayRecyclerAdapter = new ToDoOneDayRecyclerAdapter(dataSet, ToDoOneDayFragment.this);
        recyclerView.setAdapter(toDoOneDayRecyclerAdapter);

        //设置点击事件监听


        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(ToDoOneDayFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}