package com.example.reminder_calendar.ui.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.reminder_calendar.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;


import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class CalendarFragment extends Fragment {
    final int[] Colors = {
            0xFF990033, 0xFFCC6699, 0xFFFF6699, 0xFFFF3366, 0xFF993366, 0xFFCC0066, 0xFFCC0033, 0xFFFF0066, 0xFFFF0033,
            0xFFCC3399, 0xFFFF3399, 0xFFFF9999, 0xFFFF99CC, 0xFFFF0099, 0xFFCC3366, 0xFFFF66CC, 0xFFFF33CC, 0xFFFFCCFF,
            0xFFFF99FF, 0xFFFF00CC, 0xFFFF66FF, 0xFFCC33CC, 0xFFCC00FF, 0xFFFF33FF, 0xFFCC99FF, 0xFF9900CC, 0xFFFF00FF,
            0xFFCC66FF, 0xFF990099, 0xFFCC0099, 0xFFCC33FF, 0xFFCC99CC, 0xFF990066, 0xFF993399, 0xFFCC66CC, 0xFFCC00CC,
            0xFF663366, 0xFF660099, 0xFF0666FF, 0xFF0000CC, 0xFF9933CC, 0xFF666699, 0xFF660066, 0xFF333366, 0xFF0066CC,
            0xFF9900FF, 0xFF333399, 0xFF99CCFF, 0xFF9933FF, 0xFF330099, 0xFF6699FF, 0xFF9966CC, 0xFF3300CC, 0xFF003366,
            0xFF330033, 0xFF3300FF, 0xFF6699CC, 0xFF663399, 0xFF3333FF, 0xFF006699, 0xFF6633CC, 0xFF3333CC, 0xFF3399CC,
            0xFF6600CC, 0xFF0066FF, 0xFF0099CC, 0xFF9966FF, 0xFF0033FF, 0xFF66CCFF, 0xFF330066, 0xFF3366FF, 0xFF3399FF,
            0xFF6600FF, 0xFF3366CC, 0xFF003399, 0xFF6633FF, 0xFF000066, 0xFF0099FF, 0xFFCCCCFF, 0xFF000033, 0xFF33CCFF,
            0xFF9999FF, 0xFF00000F, 0xFF00CCFF, 0xFF9999CC, 0xFF000099, 0xFF6666CC, 0xFF0033CC, 0xFFFFFFCC, 0xFFFFCC00,
            0x8CC99090, 0xFF663300, 0xFFFF6600, 0xFF663333, 0xFFCC6666, 0xFFFF6666, 0xFFFF0000, 0xFFFFFF99, 0xFFFFCC66,
            0xFFFF9900, 0xFFFF9966, 0xFFCC3300, 0xFF996666, 0xFFFFCCCC, 0xFF660000, 0xFFFF3300, 0xFFFF6666, 0xFFFFCC33,
            0xFFCC6600, 0xFFFF6633, 0xFF996633, 0xFFCC9999, 0xFFFF3333, 0xFF990000, 0xFFCC9966, 0xFFFFFF33, 0xFFCC9933,
            0xFF993300, 0xFFFF9933, 0xFF330000, 0xFF993333, 0xFFCC3333, 0xFFCC0000, 0xFFFFCC99, 0xFFFFFF00, 0xFF996600,
            0xFFCC6633, 0xFF99FFFF, 0xFF33CCCC, 0xFF00CC99, 0xFF99FF99, 0xFF009966, 0xFF33FF33, 0xFF33FF00, 0xFF99CC33,
            0xFF0CCC33, 0xFF66FFFF, 0xFF66CCCC, 0xFF66FFCC, 0xFF66FF66, 0xFF009933, 0xFF00CC33, 0xFF66FF00, 0xFF336600,
            0xFF033300, 0xFF33FFFF, 0xFF339999, 0xFF99FFCC, 0xFF339933, 0xFF33FF66, 0xFF33CC33, 0xFF99FF00, 0xFF669900,
            0xFF666600, 0xFF00FFFF, 0xFF336666, 0xFF00FF99, 0xFF99CC99, 0xFF00FF66, 0xFF66FF33, 0xFF66CC00, 0xFF99CC00,
            0xFF999933, 0xFF00CCCC, 0xFF006666, 0xFF339966, 0xFF66FF99, 0xFFCCFFCC, 0xFF00FF00, 0xFF00CC00, 0xFFCCFF66,
            0xFFCCCC66, 0xFF009999, 0xFF003333, 0xFF006633, 0xFF33FF99, 0xFFCCFF99, 0xFF66CC33, 0xFF33CC00, 0xFFCCFF33,
            0xFF666633, 0xFF669999, 0xFF00FFCC, 0xFF336633, 0xFF33CC66, 0xFF99FF66, 0xFF006600, 0xFF339900, 0xFFCCFF00,
            0xFF999966, 0xFF99CCCC, 0xFF33FFCC, 0xFF669966, 0xFF00CC66, 0xFF99FF33, 0xFF003300, 0xFF99CC66, 0xFF999900,
            0xFFCCCC99, 0xFFCCFFFF, 0xFF33CC99, 0xFF66CC66, 0xFF66CC99, 0xFF00FF33, 0xFF009900, 0xFF669900, 0xFF669933,
            0xFFCCCC00, 0xFF0FFFFF, 0xFFCCCCCC, 0xFF999999, 0xFF666666, 0xFF333333, 0xFF000000
    };

    private CalendarViewModel mCalendarViewModel;
    ImageButton addButton;
    TextView monthTextView;
    TextView yearTextView;
    CalendarView mCalendarView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mCalendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        addButton = root.findViewById(R.id.btn_add);
        monthTextView = root.findViewById(R.id.text_month);
        yearTextView = root.findViewById(R.id.text_year);
        mCalendarView = root.findViewById(R.id.calendarView);

        addButton.setBackgroundResource(R.drawable.ic_add);
        monthTextView.setText(translateMonthToString(mCalendarView.getCurMonth()));
        yearTextView.setText(((Integer)mCalendarView.getCurYear()).toString());

        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar selectedCalendar = mCalendarView.getSelectedCalendar();
                Integer hour = (int)Math.floor(Math.random() * 12);
                Integer minute = (int)Math.floor((Math.random() * 60));
                Calendar schemeCalendar = getSchemeCalendar(
                        selectedCalendar,
                        selectedCalendar.getYear(),
                        selectedCalendar.getMonth(),
                        selectedCalendar.getDay(),
                        hour.toString() + ":" + minute.toString());
                mCalendarView.addSchemeDate(schemeCalendar);
            }
        });

        mCalendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                monthTextView.setText(translateMonthToString(month));
                yearTextView.setText(((Integer)year).toString());
            }
        });

        initData();
        return root;
    }

    void initData() {
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();


    }

    private Calendar getSchemeCalendar(Calendar calendar, int year, int month, int day, String text) {
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(Color.WHITE);
        calendar.setScheme(text);

        Integer colorIndex = (int)(Math.random() * Colors.length);
        calendar.addScheme(Colors[colorIndex], text);

        return calendar;
    }

    public String translateMonthToString(Integer month) {
        if (month <= 9)
            return "0" + month.toString();
        else
            return month.toString();
    }

}