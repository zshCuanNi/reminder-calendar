package com.example.reminder_calendar.ui.calendar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.reminder_calendar.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CalendarFragment extends Fragment {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient okHttpClient = new OkHttpClient();
    private static final String SERVERURL = "http://10.0.2.2:8848";
    private static final String LOCALURL = "http://10.0.2.2:8848";
    private String username = "";
    final static int GET = 0;
    final static int[] Colors = {
            0xBB990033, 0xBBCC6699, 0xBBFF6699, 0xBBFF3366, 0xBB993366, 0xBBCC0066, 0xBBCC0033, 0xBBFF0066, 0xBBFF0033,
            0xBBCC3399, 0xBBFF3399, 0xBBFF9999, 0xBBFF99CC, 0xBBFF0099, 0xBBCC3366, 0xBBFF66CC, 0xBBFF33CC, 0xBBFFCCFF,
            0xBBFF99FF, 0xBBFF00CC, 0xBBFF66FF, 0xBBCC33CC, 0xBBCC00FF, 0xBBFF33FF, 0xBBCC99FF, 0xBB9900CC, 0xBBFF00FF,
            0xBBCC66FF, 0xBB990099, 0xBBCC0099, 0xBBCC33FF, 0xBBCC99CC, 0xBB990066, 0xBB993399, 0xBBCC66CC, 0xBBCC00CC,
            0xBB663366, 0xBB660099, 0xBB0666FF, 0xBB0000CC, 0xBB9933CC, 0xBB666699, 0xBB660066, 0xBB333366, 0xBB0066CC,
            0xBB9900FF, 0xBB333399, 0xBB99CCFF, 0xBB9933FF, 0xBB330099, 0xBB6699FF, 0xBB9966CC, 0xBB3300CC, 0xBB003366,
            0xBB330033, 0xBB3300FF, 0xBB6699CC, 0xBB663399, 0xBB3333FF, 0xBB006699, 0xBB6633CC, 0xBB3333CC, 0xBB3399CC,
            0xBB6600CC, 0xBB0066FF, 0xBB0099CC, 0xBB9966FF, 0xBB0033FF, 0xBB66CCFF, 0xBB330066, 0xBB3366FF, 0xBB3399FF,
            0xBB6600FF, 0xBB3366CC, 0xBB003399, 0xBB6633FF, 0xBB000066, 0xBB0099FF, 0xBBCCCCFF, 0xBB000033, 0xBB33CCFF,
            0xBB9999FF, 0xBB00000F, 0xBB00CCFF, 0xBB9999CC, 0xBB000099, 0xBB6666CC, 0xBB0033CC, 0xBBFFFFCC, 0xBBFFCC00,
            0xBBC99090, 0xBB663300, 0xBBFF6600, 0xBB663333, 0xBBCC6666, 0xBBFF6666, 0xBBFF0000, 0xBBFFFF99, 0xBBFFCC66,
            0xBBFF9900, 0xBBFF9966, 0xBBCC3300, 0xBB996666, 0xBBFFCCCC, 0xBB660000, 0xBBFF3300, 0xBBFF6666, 0xBBFFCC33,
            0xBBCC6600, 0xBBFF6633, 0xBB996633, 0xBBCC9999, 0xBBFF3333, 0xBB990000, 0xBBCC9966, 0xBBFFFF33, 0xBBCC9933,
            0xBB993300, 0xBBFF9933, 0xBB330000, 0xBB993333, 0xBBCC3333, 0xBBCC0000, 0xBBFFCC99, 0xBBFFFF00, 0xBB996600,
            0xBBCC6633, 0xBB99FFFF, 0xBB33CCCC, 0xBB00CC99, 0xBB99FF99, 0xBB009966, 0xBB33FF33, 0xBB33FF00, 0xBB99CC33,
            0xBB0CCC33, 0xBB66FFFF, 0xBB66CCCC, 0xBB66FFCC, 0xBB66FF66, 0xBB009933, 0xBB00CC33, 0xBB66FF00, 0xBB336600,
            0xBB033300, 0xBB33FFFF, 0xBB339999, 0xBB99FFCC, 0xBB339933, 0xBB33FF66, 0xBB33CC33, 0xBB99FF00, 0xBB669900,
            0xBB666600, 0xBB00FFFF, 0xBB336666, 0xBB00FF99, 0xBB99CC99, 0xBB00FF66, 0xBB66FF33, 0xBB66CC00, 0xBB99CC00,
            0xBB999933, 0xBB00CCCC, 0xBB006666, 0xBB339966, 0xBB66FF99, 0xBBCCFFCC, 0xBB00FF00, 0xBB00CC00, 0xBBCCFF66,
            0xBBCCCC66, 0xBB009999, 0xBB003333, 0xBB006633, 0xBB33FF99, 0xBBCCFF99, 0xBB66CC33, 0xBB33CC00, 0xBBCCFF33,
            0xBB666633, 0xBB669999, 0xBB00FFCC, 0xBB336633, 0xBB33CC66, 0xBB99FF66, 0xBB006600, 0xBB339900, 0xBBCCFF00,
            0xBB999966, 0xBB99CCCC, 0xBB33FFCC, 0xBB669966, 0xBB00CC66, 0xBB99FF33, 0xBB003300, 0xBB99CC66, 0xBB999900,
            0xBBCCCC99, 0xBBCCFFFF, 0xBB33CC99, 0xBB66CC66, 0xBB66CC99, 0xBB00FF33, 0xBB009900, 0xBB669900, 0xBB669933,
            0xBBCCCC00, 0xBB0FFFFF, 0xBBCCCCCC, 0xBB999999, 0xBB666666, 0xBB333333, 0xBB000000
    };
    List<String> headline = new ArrayList<>();
    List<String> detail = new ArrayList<>();
    List<String> strDeadline = new ArrayList<>();
    List<LocalDateTime> deadline = new ArrayList<>();

    private CalendarViewModel mCalendarViewModel;
    ImageButton detailButton;
    ImageButton addButton;
    TextView monthTextView;
    TextView yearTextView;
    CalendarView mCalendarView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mCalendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        detailButton = root.findViewById(R.id.btn_detail);
        addButton = root.findViewById(R.id.btn_add);
        monthTextView = root.findViewById(R.id.text_month);
        yearTextView = root.findViewById(R.id.text_year);
        mCalendarView = root.findViewById(R.id.calendarView);

        detailButton.setBackgroundResource(R.drawable.ic_detail);
        addButton.setBackgroundResource(R.drawable.ic_add);
        monthTextView.setText(translateMonthToString(mCalendarView.getCurMonth()));
        yearTextView.setText(((Integer)mCalendarView.getCurYear()).toString());

        detailButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 通过mCalendar.getSelectedCalendar获得当前时期
                // 跳转到为某一天添加事项
            }
        });
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar selectedCalendar = mCalendarView.getSelectedCalendar();
                Integer hour = (int)Math.floor(Math.random() * 24);
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

        // initData();

        return root;
    }

    void initData() {
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        getDataFromGet(SERVERURL + "/api/allMemos?username=" + username, GET);
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

    /**
     * 解析get返回的json包
     * @param json get返回的json包
     * @throws JSONException 解析出错
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void parseJsonPacket(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);

        JSONArray memoList = jsonObject.getJSONArray("data");
        // update data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        LocalDateTime dd;
        Calendar schemeCalendar;

        for (int i = 0; i < memoList.length(); i++) {
            headline.add(memoList.getJSONObject(i).getString("headline"));
            detail.add(memoList.getJSONObject(i).getString("detail"));
            strDeadline.add(memoList.getJSONObject(i).getString("deadline"));

            dd = LocalDateTime.parse(strDeadline.get(i), formatter);
            deadline.add(dd);

            schemeCalendar = getSchemeCalendar(
                    new Calendar(),
                    dd.getYear(),
                    dd.getMonthValue(),
                    dd.getDayOfMonth(),
                    ((Integer)dd.getHour()).toString() + ":" + ((Integer)dd.getMinute()).toString());
            mCalendarView.addSchemeDate(schemeCalendar);
        }
    }

    // 处理get请求的回调函数
    private Handler getHandler = new Handler(new Handler.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            try {
                if (msg.what == GET) {
                    parseJsonPacket((String) msg.obj);
                    // updateView();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
    });

    // 使用get获取数据
    private void getDataFromGet(String url, int what) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Message msg;
                try {
                    if (what == GET) {
                        String result = get(url);
                        msg = Message.obtain();
                        msg.what = GET;
                        msg.obj = result;
                        getHandler.sendMessage(msg);
                    }
                } catch (java.io.IOException IOException) {
                    Log.e("TAG", "get failed.");
                }
            }
        }.start();
    }

    /**
     * Okhttp的get请求
     * @param url
     * @return 服务器返回的字符串
     * @throws IOException
     */
    private String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }
}