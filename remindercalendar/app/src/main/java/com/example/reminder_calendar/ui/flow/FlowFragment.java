package com.example.reminder_calendar.ui.flow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.reminder_calendar.R;

public class FlowFragment extends Fragment {

    private FlowViewModel flowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        flowViewModel =
                new ViewModelProvider(this).get(FlowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_flow, container, false);
        //final TextView textView = root.findViewById(R.id.text_flow);
        flowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               // textView.setText(s);
            }
        });
        return root;
    }
}