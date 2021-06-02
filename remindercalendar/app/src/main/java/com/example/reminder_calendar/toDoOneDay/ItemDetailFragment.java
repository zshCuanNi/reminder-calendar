package com.example.reminder_calendar.toDoOneDay;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.reminder_calendar.R;
import com.example.reminder_calendar.databinding.FragmentItemDetailBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ItemDetailFragment extends Fragment {

    private FragmentItemDetailBinding binding;
    private TextView dateText;
    private EditText editText;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentItemDetailBinding.inflate(inflater, container, false);

        //初始化选择日期组件
        dateText = binding.edittextSecondDate;
        calendar = Calendar.getInstance();
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateText.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        //绑定日期点击
        dateText.setOnClickListener(v->{
            new DatePickerDialog(
                    getActivity(),
                    onDateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        editText = binding.edittextSecond;
        Bundle bundle = getArguments();
        String text = bundle.getString("text","null");
        editText.setText(text);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(ItemDetailFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}