package com.example.reminder_calendar.ui.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.reminder_calendar.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class CalendarFragment extends Fragment {

    private CalendarViewModel mCalendarViewModel;
    TextView monthTextView;
    TextView yearTextView;
    CalendarView mCalendarView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mCalendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        monthTextView = root.findViewById(R.id.text_month);
        yearTextView = root.findViewById(R.id.text_year);
        mCalendarView = root.findViewById(R.id.calendarView);

        monthTextView.setText(translateMonthToString(mCalendarView.getCurMonth()));
        yearTextView.setText(((Integer)mCalendarView.getCurYear()).toString());

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

        Map<String, Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month, 3, "假").toString(),
                getSchemeCalendar(year, month, 3, "假"));
        map.put(getSchemeCalendar(year, month, 6, "事").toString(),
                getSchemeCalendar(year, month, 6, "事"));
        map.put(getSchemeCalendar(year, month, 9, "议").toString(),
                getSchemeCalendar(year, month, 9, "议"));
        map.put(getSchemeCalendar(year, month, 13, "记").toString(),
                getSchemeCalendar(year, month, 13, "记"));
        map.put(getSchemeCalendar(year, month, 14, "记").toString(),
                getSchemeCalendar(year, month, 14, "记"));
        map.put(getSchemeCalendar(year, month, 15, "假").toString(),
                getSchemeCalendar(year, month, 15, "假"));
        map.put(getSchemeCalendar(year, month, 18, "记").toString(),
                getSchemeCalendar(year, month, 18, "记"));
        map.put(getSchemeCalendar(year, month, 25, "假").toString(),
                getSchemeCalendar(year, month, 25, "假"));
        map.put(getSchemeCalendar(year, month, 27, "多").toString(),
                getSchemeCalendar(year, month, 27, "多"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);
    }

    private Calendar getSchemeCalendar(int year, int month, int day, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(Color.WHITE);
        calendar.setScheme(text);
        calendar.addScheme(0xFFa8b015, "rightTop");
        calendar.addScheme(0xFF423cb0, "leftTop");
        calendar.addScheme(0xFF643c8c, "bottom");

        return calendar;
    }

    public String translateMonthToString(Integer month) {
        if (month <= 9)
            return "0" + month.toString();
        else
            return month.toString();
    }

}