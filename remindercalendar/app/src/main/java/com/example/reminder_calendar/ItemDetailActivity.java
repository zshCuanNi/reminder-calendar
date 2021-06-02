package com.example.reminder_calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.reminder_calendar.databinding.ActivityItemDetailBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ItemDetailActivity extends AppCompatActivity {

    private ActivityItemDetailBinding binding;
    private TextView dateText;
    private EditText editText;
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateText.setText(simpleDateFormat.format(calendar.getTime()));
        }
    };
    ;
    //private FinishOnClickInterface finishOnClickInterface;
    private Integer position;


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

        editText = binding.edittextSecond;
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        if(bundle!=null){
            String text = bundle.getString("text", "null");
            position = bundle.getInt("position");
            editText.setText(text);
        }


        //完成按钮
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishEditing();
            }
        });
    }

    //截获返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishEditing();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void finishEditing(){
        Bundle bundle = new Bundle();
        if(position!=null)
            bundle.putInt("position", position);
        bundle.putString("text", editText.getText().toString());
        Intent intent = new Intent();
        intent.putExtra("bundle", bundle);
        setResult(0, intent);
        finish();
    }
}