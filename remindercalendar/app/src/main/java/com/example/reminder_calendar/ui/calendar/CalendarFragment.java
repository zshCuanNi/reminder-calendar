package com.example.reminder_calendar.ui.calendar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.reminder_calendar.FlagValues;
import com.example.reminder_calendar.HomeActivity;
import com.example.reminder_calendar.HttpServer;
import com.example.reminder_calendar.ItemDetailActivity;
import com.example.reminder_calendar.ListContent;
import com.example.reminder_calendar.R;
import com.example.reminder_calendar.ToDoOneDayActivity;
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
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.reminder_calendar.ui.calendar.ColorValues.Colors;

public class CalendarFragment extends Fragment {

    private static final String SERVERURL = "http://10.0.2.2:8848";
    private static final String LOCALURL = "http://10.0.2.2:8848";

    final static int GET = 0;

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
        monthTextView.setText(translateIntegerToString(mCalendarView.getCurMonth()));
        yearTextView.setText(((Integer)mCalendarView.getCurYear()).toString());

        detailButton.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // 跳转到某一天的事件流

                // 准备intent
                // 通过mCalendar.getSelectedCalendar获得当前时期
                Calendar selectedCalendar = mCalendarView.getSelectedCalendar();
                Integer year = selectedCalendar.getYear();
                Integer month = selectedCalendar.getMonth();
                Integer day = selectedCalendar.getDay();
                String date = year.toString() + "-" + translateIntegerToString(month) + "-" + translateIntegerToString(day);

                Bundle bundle = new Bundle();
                bundle.putString("date", date);
                Intent intent = new Intent(getActivity(), ToDoOneDayActivity.class);
                intent.putExtra("bundle",bundle);

                // 准备当天事件的所有备忘
                ListContent.clearData();
                for (int j = 0; j < deadline.size(); j++) {
                    LocalDateTime i = deadline.get(j);
                    if (i.getYear() == year && i.getMonthValue() == month && i.getDayOfMonth() == day) {
                        ListContent.titleDataSet.add(headline.get(j));
                        ListContent.contentDataSet.add(detail.get(j));
                        ListContent.timeDataSet.add(strDeadline.get(j));
                    }
                }
                Log.e("s", ""+ListContent.titleDataSet.size());
                startActivityForResult(intent, 0);
            }
        });
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                // for demo
//                Calendar selectedCalendar = mCalendarView.getSelectedCalendar();
//                Integer hour = (int)Math.floor(Math.random() * 24);
//                Integer minute = (int)Math.floor((Math.random() * 60));
//                Calendar schemeCalendar = getSchemeCalendar(
//                        selectedCalendar,
//                        selectedCalendar.getYear(),
//                        selectedCalendar.getMonth(),
//                        selectedCalendar.getDay(),
//                        hour.toString() + ":" + minute.toString());
//                mCalendarView.addSchemeDate(schemeCalendar);

                // 跳转到加入一条备忘
                // 通过mCalendar.getSelectedCalendar获得当前时期
                Calendar selectedCalendar = mCalendarView.getSelectedCalendar();
                Integer year = selectedCalendar.getYear();
                Integer month = selectedCalendar.getMonth();
                Integer day = selectedCalendar.getDay();
                String date = year.toString() + "-" + translateIntegerToString(month) + "-" + translateIntegerToString(day);

                Bundle bundle = new Bundle();
                bundle.putString("date",date);
                bundle.putString("time","00:00");
                bundle.putInt("requestCode", FlagValues.newMemoFlag);
                Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
                intent.putExtra("bundle",bundle);
                //requestCode为1，代表是新增事件
                startActivityForResult(intent,FlagValues.newMemoFlag);
            }
        });

        mCalendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                monthTextView.setText(translateIntegerToString(month));
                yearTextView.setText(((Integer)year).toString());
            }
        });

        initData();

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("result", "1");
        initData();
    }

    void initData() {
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        getDataFromGet(HttpServer.SERVERURL + "/api/allMemos?username=" + ListContent.username, GET);
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

    public String translateIntegerToString(Integer integer) {
        return integer <= 9 ? "0" + integer.toString():integer.toString();
    }

    /**
     * 解析get返回的json包
     * @param json get返回的json包
     * @throws JSONException 解析出错
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void parseJsonPacket(String json) throws JSONException {
        Log.e("home obj", json);
        JSONObject jsonObject = new JSONObject(json);

        JSONArray memoList = jsonObject.getJSONArray("data");
        // update data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dd;
        Calendar schemeCalendar;

        HashMap<String, Calendar> dateCalendars = new HashMap<>();
        headline.clear();
        detail.clear();
        strDeadline.clear();
        deadline.clear();
        ListContent.clearData();
        for (int i = 0; i < memoList.length(); i++) {
            Log.e("i", ""+i);
            headline.add(memoList.getJSONObject(i).getString("headline"));
            detail.add(memoList.getJSONObject(i).getString("detail"));
            strDeadline.add(memoList.getJSONObject(i).getString("deadline"));
            //Calendar selectedCalendar = mCalendarView.getSelectedCalendar();
            dd = LocalDateTime.parse(strDeadline.get(i), formatter);
            deadline.add(dd);
            String date = ""+dd.getYear()+dd.getMonth()+dd.getDayOfMonth();
            Calendar calendar;
            if(!dateCalendars.containsKey(date)){
                calendar = new Calendar();
                dateCalendars.put(date, calendar);
            }else{
                calendar = dateCalendars.get(date);
            }
            Log.e("time", ((Integer)dd.getHour()).toString() + ":" + ((Integer)dd.getMinute()).toString());
            schemeCalendar = getSchemeCalendar(
                    calendar,
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
        Response response = HttpServer.okHttpClient.newCall(request).execute();
        return response.body().string();
    }
}