package com.example.reminder_calendar.ui.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.haibin.calendarview.WeekBar;
import com.example.reminder_calendar.R;

public final class SolarWeekBar extends WeekBar {
    public SolarWeekBar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.solar_week_bar, this, true);
        setBackgroundColor(context.getResources().getColor(R.color.galaxy_background));
    }

    /**
     * 当周起始发生变化，使用自定义布局需要重写这个方法，避免出问题
     *
     * @param weekStart 周起始
     */
    @Override
    protected void onWeekStartChange(int weekStart) {
        for (int i = 0; i < getChildCount(); i++) {
            ((TextView) getChildAt(i)).setText(getWeekString(i, weekStart));
        }
    }

    /**
     * 或者周文本，这个方法仅供父类使用
     *
     * @param index     index
     * @param weekStart weekStart
     * @return 或者周文本
     */
    private String getWeekString(int index, int weekStart) {
        String[] weeks = { "SUN", "MON", "TUE", "THU", "WED", "FRI", "SAT"};

        if (weekStart == 1) {
            return weeks[index];
        }
        if (weekStart == 2) {
            return weeks[index == 6 ? 0 : index + 1];
        }
        return weeks[index == 0 ? 6 : index - 1];
    }
}
