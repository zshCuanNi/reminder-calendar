package com.example.reminder_calendar.ui.calendar;

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
import com.haibin.calendarview.CalendarView;

import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;

public class CalendarFragment extends Fragment {

    private CalendarViewModel calendarViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        Log.e("TAG", "onCreateView: get_in");
//        final TextView textView = root.findViewById(R.id.text_calendar);
//        calendarViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        TextView monthTextView = root.findViewById(R.id.text_month);
        TextView yearTextView = root.findViewById(R.id.text_year);
        CalendarView calendarView = root.findViewById(R.id.calendarView);

        monthTextView.setText(translateMonthToString(calendarView.getCurMonth()));
        yearTextView.setText(((Integer)calendarView.getCurYear()).toString());

        Log.e("TAG", calendarView.getMonthViewPager().toString());

        calendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                monthTextView.setText(translateMonthToString(month));
                yearTextView.setText(((Integer)year).toString());
            }
        });
        return root;
    }

    public String translateMonthToString(Integer month) {
        if (month <= 9)
            return "0" + month.toString();
        else
            return month.toString();
    }

}